from q4 import CanvasClickHandler

print("Python script running...")

for i in range(0, 200, 10):
    canvas.moveTo(100 + i, 100 + i)
    canvas.colour("red")
    canvas.lineTo(100 + i, 200 + i)
    canvas.colour("green")
    canvas.lineTo(200 + i, 200 + i)
    canvas.colour("blue")
    canvas.lineTo(200 + i, 100 + i)
    canvas.colour("yellow")
    canvas.lineTo(100 + i, 100 + i)


class Handler(CanvasClickHandler):
    def onClick(self, x, y):
        canvas.colour("red")
        canvas.moveTo(x - 3, y - 3)
        canvas.lineTo(x + 3, y + 3)
        canvas.moveTo(x - 3, y + 3)
        canvas.lineTo(x + 3, y - 3)

canvas.registerHandler(Handler())
