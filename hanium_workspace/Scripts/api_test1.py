from flask import Flask, render_template,request, jsonify
import datetime
import tensorflow as tf
import numpy as np
import os, sys, time

import torch
import torch.nn as nn
import torch.nn.functional as F

import cv2
import glob
import warnings
warnings.filterwarnings("ignore")
from tqdm.notebook import tqdm

app = Flask(__name__)

device = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")

sys.path.insert(0,"C:\\Users\\USER\\Desktop\\2021\\2021HANIUM\\hanium\\hanium_workspace\\Lib\\site-packages\\blazeface-pytorch")
sys.path.insert(0,"C:\\Users\\USER\\Desktop\\2021\\2021HANIUM\\hanium\\hanium_workspace\\Lib\\site-packages\\deepfakes-inference-demo")
from blazeface import BlazeFace

facedet = BlazeFace().to(device)
facedet.load_weights("C:\\Users\\USER\\Desktop\\2021\\2021HANIUM\\hanium\\hanium_workspace\\Lib\\site-packages\\blazeface-pytorch\\blazeface.pth")
facedet.load_anchors("C:\\Users\\USER\\Desktop\\2021\\2021HANIUM\\hanium\\hanium_workspace\\Lib\\site-packages\\blazeface-pytorch\\anchors.npy")
_ = facedet.train(False)

from helpers.read_video_1 import VideoReader
from helpers.face_extract_1 import FaceExtractor

frames_per_video = 20

video_reader = VideoReader()
video_read_fn = lambda x: video_reader.read_frames(x, num_frames=frames_per_video)
face_extractor = FaceExtractor(video_read_fn, facedet)

input_size = 150

from torchvision.transforms import Normalize

mean = [0.485, 0.456, 0.406]
std = [0.229, 0.224, 0.225]
normalize_transform = Normalize(mean, std)

def isotropically_resize_image(img, size, resample=cv2.INTER_AREA):
    h, w = img.shape[:2]
    if w > h:
        h = h * size // w
        w = size
    else:
        w = w * size // h
        h = size

    resized = cv2.resize(img, (w, h), interpolation=resample)
    return resizedp


def make_square_image(img):
    h, w = img.shape[:2]
    size = max(h, w)
    t = 0
    b = size - h
    l = 0
    r = size - w
    return cv2.copyMakeBorder(img, t, b, l, r, cv2.BORDER_CONSTANT, value=0)

from pytorchcv.model_provider import get_model
model = get_model("xception", pretrained=False)
model = nn.Sequential(*list(model.children())[:-1]) # Remove original output layer

model[0].final_block.pool = nn.Sequential(nn.AdaptiveAvgPool2d(1))

class Head(torch.nn.Module):
  def __init__(self, in_f, out_f):
    super(Head, self).__init__()
    
    self.f = nn.Flatten()
    self.l = nn.Linear(in_f, 512)
    self.d = nn.Dropout(0.5)
    self.o = nn.Linear(512, out_f)
    self.b1 = nn.BatchNorm1d(in_f)
    self.b2 = nn.BatchNorm1d(512)
    self.r = nn.ReLU()

  def forward(self, x):
    x = self.f(x)
    x = self.b1(x)
    x = self.d(x)

    x = self.l(x)
    x = self.r(x)
    x = self.b2(x)
    x = self.d(x)

    out = self.o(x)
    return out

class FCN(torch.nn.Module):
  def __init__(self, base, in_f):
    super(FCN, self).__init__()
    self.base = base
    self.h1 = Head(in_f, 1)
  
  def forward(self, x):
    x = self.base(x)
    return self.h1(x)

net = []
model = FCN(model, 2048)
#model = model.cuda()
model.load_state_dict(torch.load('C:\\Users\\USER\\Desktop\\2021\\2021HANIUM\\hanium\\model.pth',map_location=device))
model.eval()

def predict_on_video(video_path, batch_size):
    try:
        # Find the faces for N frames in the video.
        faces = face_extractor.process_video(video_path)

        # Only look at one face per frame.
        face_extractor.keep_only_best_face(faces)
        
        if len(faces) > 0:
            # NOTE: When running on the CPU, the batch size must be fixed
            # or else memory usage will blow up. (Bug in PyTorch?)
            x = np.zeros((batch_size, input_size, input_size, 3), dtype=np.uint8)

            # If we found any faces, prepare them for the model.
            n = 0
            for frame_data in faces:
                for face in frame_data["faces"]:
                    # Resize to the model's required input size.
                    # We keep the aspect ratio intact and add zero
                    # padding if necessary.                    
                    resized_face = isotropically_resize_image(face, input_size)
                    resized_face = make_square_image(resized_face)

                    if n < batch_size:
                        x[n] = resized_face
                        n += 1
                    else:
                        print("WARNING: have %d faces but batch size is %d" % (n, batch_size))
                    
                    # Test time augmentation: horizontal flips.
                    # TODO: not sure yet if this helps or not
                    #x[n] = cv2.flip(resized_face, 1)
                    #n += 1

            if n > 0:
                x = torch.tensor(x, device=device).float()

                # Preprocess the images.
                x = x.permute((0, 3, 1, 2))

                for i in range(len(x)):
                    x[i] = normalize_transform(x[i] / 255.)
#                     x[i] = x[i] / 255.

                # Make a prediction, then take the average.
                with torch.no_grad():
                    y_pred = model(x)
                    y_pred = torch.sigmoid(y_pred.squeeze())
                    return (y_pred[:n].mean().item()) * 100

    except Exception as e:
        print("Prediction error on video %s: %s" % (video_path, str(e)))

    return 0.5

from concurrent.futures import ThreadPoolExecutor

def predict_on_video_set(videos, num_workers):
    def process_file(i):
        filename = videos[i]
        y_pred = predict_on_video(filename, batch_size=frames_per_video)
        return y_pred

    with ThreadPoolExecutor(max_workers=num_workers) as ex:
        predictions = ex.map(process_file, range(len(videos)))

    return list(predictions)

from werkzeug.utils import secure_filename

@app.route("/",methods = ['GET','POST'])
def index():
    if request.method == 'GET':
        return render_template('index.html')
    if request.method == 'POST':
        file = request.files['file']
        #file = request.form['video']
        fname = secure_filename(file.filename)
        file_path = 'C:\\Users\\USER\\Desktop\\2021\\2021HANIUM\\hanium\\requested_images\\' + fname
        file.save(file_path)
    
    res = predict_on_video_set([file_path],num_workers = 4)
    return render_template('index.html',res=res[0])

@app.route("/result2",methods = ['POST'])
def index2():
    file = request.files['file']
    #file = request.form['video']
    fname = secure_filename(file.filename)
    file_path = 'C:\\Users\\USER\\Desktop\\2021\\2021HANIUM\\hanium\\requested_images\\' + fname
    file.save(file_path)
    
    res = predict_on_video_set([file_path],num_workers = 4)
    return jsonify({ 'success':True, 'probability' : res[0] })

@app.route("/result", methods = ['POST'])
def handle_request():
    import flask, werkzeug
    files_ids = list(flask.request.files)
    image_num = 1
    for file_id in files_ids:
        #print("\nSaving Image ", str(image_num), "/", len(files_ids))
        imagefile = flask.request.files[file_id]
        filename = werkzeug.utils.secure_filename(imagefile.filename)
        #print("Image Filename : " + imagefile.filename)
        file_path = 'C:\\Users\\USER\\Desktop\\2021\\2021HANIUM\\hanium\\requested_images\\'
        timestr = time.strftime("%Y%m%d-%H%M%S")
        imagefile.save(file_path + timestr+'_'+filename)
        image_num = image_num + 1

        res = predict_on_video_set([file_path + timestr+'_'+filename],num_workers = 4)
    
    return str(res[0])

if __name__ == '__main__':
    app.run(host = "0.0.0.0",port=5000,debug=True)