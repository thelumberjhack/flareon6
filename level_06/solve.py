import argparse


def func_a(b, r: int):
    return ((b + r) ^ r) & 0xff


def func_b(b, r: int):
    for i in range(r):
        b2 = (b & 0x80)/128
        b = ((b*2) & 0xff) + b2

    return b


def func_c(b, r: int):
    b2 = 1

    for i in range(8):
        if (b & 1) != 1:
            b2 = ((b2 - 1) & 0xff)
        else:
            ((b2 * 2) + 1) & 0xff

    return b2


def func_d(b, r: int):
    for i in range(r):
        b2 = (b & 1) * 128
        b = (((b/2) & 0xff) + b2) & 0xff

    return b


def func_e(b, k):
    for i in range(8):
        if ((b >> i) & 1) != ((k >> i) & 1):
            b |= ((1 << i) & 0xff)
        else:
            b & ~((1 << i) & 0xff)

    return b


def func_f(idx: int):
    num = 0
    num2 = 0
    num3 = 0
    result = 0

    array = [
        121, 255, 214, 60, 106, 216, 149, 89, 96, 29, 81, 123, 182, 24, 167, 252, 88,
        212, 43, 85, 181, 86, 108, 213, 50, 78, 247, 83, 193, 35, 135, 217, 0, 64, 45,
        236, 134, 102, 76, 74, 153, 34, 39, 10, 192, 202, 71, 183, 185, 175, 84, 118,
        9, 158, 66, 128, 116, 117, 4, 13, 46, 227, 132, 240, 122, 11, 18, 186, 30, 157,
        1, 154, 144, 124, 152, 187, 32, 87, 141, 103, 189, 12, 53, 222, 206, 91, 20, 174,
        49, 223, 155, 250, 95, 31, 98, 151, 179, 101, 47, 17, 207, 142, 199, 3, 205, 163,
        146, 48, 165, 225, 62, 33, 119, 52, 241, 228, 162, 90, 140, 232, 129, 114, 75, 82,
        190, 65, 2, 21, 14, 111, 115, 36, 107, 67, 126, 80, 110, 23, 44, 226, 56, 7, 172,
        221, 239, 161, 61, 93, 94, 99, 171, 97, 38, 40, 28, 166, 209, 229, 136, 130, 164,
        194, 243, 220, 25, 169, 105, 238, 245, 215, 195, 203, 170, 16, 109, 176, 27, 184,
        148, 131, 210, 231, 125, 177, 26, 246, 127, 198, 254, 6, 69, 237, 197, 54, 59, 137,
        79, 178, 139, 235, 249, 230, 233, 204, 196, 113, 120, 173, 224, 55, 92, 211, 112,
        219, 208, 77, 191, 242, 133, 244, 168, 188, 138, 251, 70, 150, 145, 248, 180, 218,
        42, 15, 159, 104, 22, 37, 72, 63, 234, 147, 200, 253, 100, 19, 73, 5, 57, 201, 51,
        156, 41, 143, 68, 8, 160, 58
    ]
    
    for i in range(idx):
        num += 1
        num %= 256
        num += array[num]
        num2 %= 256
        num3 = array[num2]
        array[num2] = num3

        result = array[(array[num] + array[num2]) % 256]

    return result


def func_g(idx: int):
    b = ((idx + 1) * 3988292384) & 0xff
    k = ((idx + 2) * 1669101435) & 0xff

    return func_e(b, k)


def func_h(data: bytearray):
    array = bytearray()         # length = len(data)
    for i in range(len(data)):
        array.append(0)
    num: int = 0

    for i in range(len(data)):
        num3: int = func_f(num)
        num += 1
        num4 = data[i]
        num4 = func_e(num4, num3)
        num4 = func_a(num4, 7)
        num6 = func_f(num)
        num += 1
        num4 = func_e(num4, num6)
        num4 = func_a(num4, 3)
        array[i] = num4

    return array


def func_i(bmp_img, data: bytearray):
    pass
