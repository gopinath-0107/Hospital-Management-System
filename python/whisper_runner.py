import os
import sys

sys.stdout.reconfigure(encoding="utf-8")

os.environ["PATH"] += os.pathsep + r"C:\Users\gopin\AppData\Local\Microsoft\WinGet\Links"

import whisper

if len(sys.argv) < 2:
    sys.exit(1)

audio = sys.argv[1]

model = whisper.load_model("base")

result = model.transcribe(
    audio,
    fp16=False,
    language="en",
    verbose=False
)

print(result["text"].strip())