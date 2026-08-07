from PIL import Image

img = Image.open('assets/hand.png')
# .ico can contain multiple sizes, but a single size is fine
img.save('assets/hand.ico', format='ICO', sizes=[(256, 256)])
print("Converted to hand.ico")
