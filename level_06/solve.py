import os
import sys
import argparse
from PIL import Image


def extract_data(bmpdata):
    """ Extracts the scrambled data from the image.
    """
    extracted = None

    red, green, blue = bmpdata.split()

    for x in range(bmpdata.size[0]):
        for y in range(bmpdata.size[1]):
            rpixel = red.getpixel((x, y))
            gpixel = green.getpixel((x, y))
            bpixel = blue.getpixel((x, y))

            print(f"{rpixel} {gpixel} {bpixel}")
            return # prototyping solution, don't need to display all pixels

    return extracted


def main():
    parser = argparse.ArgumentParser(
        prog='bmpseek'
    )

    parser.add_argument('bmp', type=str)

    args = parser.parse_args()

    input = os.path.abspath(args.bmp)

    try:
        print(f"Opening {input}")
        bmp = Image.open(input)
        print(f"{bmp.format} {bmp.size} {bmp.mode}")

        data = extract_data(bmp)

    except IOError:
        print(f"Can't read {input}")
        return -1

    else:
        print("Done playing hide & seek.")
        return 0

if __name__ == "__main__":
    sys.exit(main())
