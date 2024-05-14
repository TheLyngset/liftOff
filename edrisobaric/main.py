import os
import threading


def now():
    os.system("python3 app_now/app.py")
def in_3():
    os.system("python3 app_in_3/app.py")

threading.Thread(target=now).start()
threading.Thread(target=in_3).start()