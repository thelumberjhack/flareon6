# Flare-on 6: Level 1 - Stage 3
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


def cat_fact(s: list, i: int, j: int) -> None:
    """ Exchanges 2 items from a list of integers.
    """
    b = s[i]
    s[i] = s[j]
    s[j] = b


def invert(cat: list) -> list:
    """ Creates an S-box-like array.
    """
    array = [c for c in range(256)]
    j: int = 0
    num: int = 0
    while j < 256:
        num = num + cat[j % len(cat)] + array[j] & 255
        cat_fact(array, j, num)
        j+=1

    return array


def initcat(cat: str, data: str) -> str:
    """ Initialize Stage 3 Memecat
        Cat -> base64encode(previous_code)
        data -> new weapon code
    """
    cat = [c for c in cat]
    s = invert(cat)
    i: int = 0
    j: int = 0
    res = ""
    for c in data:
        i = (i + 1) & 255
        j = (j + s[i]) & 255
        cat_fact(s, i, j)

        res += chr((ord(c)^s[(s[i] + s[j]) & 255]) & 255)

    return res


if __name__ == "__main__":
    from base64 import b64encode

    prior_weapon_code = b64encode("Bagel_Cannon".encode())
    weapon_code = initcat(prior_weapon_code, "".join([chr(c) for c in battle_cat]))

    print(weapon_code)
