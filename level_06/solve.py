import os
import sys
import argparse


def func_b(b, r):
    """
    """
    for i in range(r):
        b2 = int((b & 0x80)/128) & 0xff
        b = (((b * 2) & 0xff) + b2) & 0xff

    return b


def func_d(b, r):
    """
    """
    for i in range(r):
        b2 = ((b & 1)*128) & 0xff
        b = ((int(b / 2) & 0xff) + b2) & 0xff

    return b


def func_e(b, k):
    for i in range(8):
        if ((b >> i) & 1) != ((k >> i) & 1):
            b |= (1 << i) & 0xff
        else:
            b &= ( ~(1 << i)) & 0xff

    return b


def func_g(idx):
    b = ((idx + 1) * 3988292384) & 0xff
    k = ((idx + 2) * 1669101435) & 0xff

    return func_e(b, k)

def func_h(data):
    dlength = len(data)
    array = bytearray(dlength)
    num = 0
    for i in range(dlength):
        num3 = func_g(num)
        num += 1
        num4 = data[i]
        num4 = func_e(num4, num3)
        num4 = func_b(num4, 7)
        num6 = func_g(num)
        num += 1
        num4 = func_e(num4, num6)
        num4 = func_d(num4, 3)

        array[i] = num4

    return array


def extract_data(bmpdata):
    """ Extracts the scrambled data from the image.
    """
    extracted = bytearray()
    blength = len(bmpdata)

    
    for x in range(0, blength, 3):
        rpixel = bmpdata[x+2]
        gpixel = bmpdata[x+1]
        bpixel = bmpdata[x]

        r = rpixel & 7
        g = (gpixel & 7) << 3
        b = (bpixel & 3) << 6

        extracted.append(r | g | b)

    return extracted


def unscramble(data):
    """ Unscramble data to original file.
    """
    pass


def main():
    parser = argparse.ArgumentParser(
        prog='bmpseek'
    )

    parser.add_argument('bmp', type=str)

    args = parser.parse_args()

    input = os.path.abspath(args.bmp)

    try:
        print(f"Opening {input}")

        bmp = None

        with open(input, 'rb') as fh:
            bmp = fh.read()[0x36:]          # don't care about header.

        data = extract_data(bmp)

        test = func_h(data)

        with open('./test.bmp', 'wb') as fh:
            fh.write(test)


    except IOError:
        print(f"Can't read {input}")
        return -1

    else:
        print("Done playing hide & seek.")
        return 0

if __name__ == "__main__":
    sys.exit(main())
