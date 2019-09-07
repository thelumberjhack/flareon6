import hashlib, io, lzma, pkgutil, random, struct, sys, time
from ctypes import *
print('LOADING...')
BOUNCE = pkgutil.get_data('this', 'key')

def ho(h, g={}):
    k = bytes.fromhex(format(h, 'x')).decode()
    return g.get(k, k)


a = 1702389091
b = 482955849332
g = ho(29516388843672123817340395359, globals())    # builtins module
aa = getattr(g, ho(a))      # exec
bb = getattr(g, ho(b))      # print
a ^= b                      # a = 481423330071
b ^= a                      # b = 1702389091
a ^= b                      # a = 482955849332 (= original b)
setattr(g, ho(a), aa)       # g, print, exec function
setattr(g, ho(b), bb)       # g, exec, print function

# Now print and exec functions are switched in the builtins module.

def eye(face):
    leg = io.BytesIO()
    for arm in face.splitlines():
        arm = arm[len(arm.rstrip(b' \t')):]
        leg.write(arm)

    face = leg.getvalue()
    bell = io.BytesIO()
    x, y = (0, 0)
    for chuck in face:
        taxi = {9:0, 
         32:1}.get(chuck)
        if taxi is None:
            continue
        x, y = x | taxi << y, y + 1
        if y > 7:
            bell.write(bytes([x]))
            x, y = (0, 0)

    return bell.getvalue()


def fire(wood, bounce):
    meaning = bytearray(wood)
    bounce = bytearray(bounce)
    regard = len(bounce)
    manage = list(range(256))

    def prospect(*financial):
        return sum(financial) % 256

    def blade(feel, cassette):
        cassette = prospect(cassette, manage[feel])
        manage[feel], manage[cassette] = manage[cassette], manage[feel]
        return cassette

    cassette = 0
    for feel in range(256):
        cassette = prospect(cassette, bounce[(feel % regard)])
        cassette = blade(feel, cassette)

    cassette = 0
    for pigeon, _ in enumerate(meaning):
        feel = prospect(pigeon, 1)
        cassette = blade(feel, cassette)
        meaning[pigeon] ^= manage[prospect(manage[feel], manage[cassette])]

    return bytes(meaning)


for i in range(256):
    try:
        print(lzma.decompress(fire(eye(__doc__.encode()), bytes([i]) + BOUNCE)))
    except Exception:
        pass
# okay decompiling cleanup.pyc
