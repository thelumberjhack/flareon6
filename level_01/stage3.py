from base64 import b64decode, b64encode
battle_cat = [
    95,
    193,
    50,
    12,
    127,
    228,
    98,
    6,
    215,
    46,
    200,
    106,
    251,
    121,
    186,
    119,
    109,
    73,
    35,
    14,
    20
]

prior_weapon_code = b64encode("Bagel_Canon")

def cat_fact(s: str, i: int, j: int):
    b = s[i]
    s[i] = s[j]
    s[j] = b

def invert(cat: str) -> str:
    array = [c for c in range(256)]
    j: int = 0
    num: int = 0
    while j < 256:
        num = num + cat[j % len(cat)] + array[j] & 255
        cat_fact(array, j, num)
        j+=1

    return array

    

def solve(cat: bytes, data: bytes) -> list:
    s = invert(cat)
    i: int = 0
    j: int = 0
    

