# Flare-on 6: Level 1 - Stage 2
def is_valid_weapon_code() -> None:
    result = [
        '\u0003',
		'"',
		'"',
		'"',
		'%',
		'\u0014',
		'\u000e',
		'.',
		'?',
		'=',
		':',
		'9'
    ]

    key = [ord(c) for c in result]

    for i in range(len(result)):
        key[i] ^= (65 + i*2)

    print("".join([chr(c) for c in key]))

if __name__ == "__main__":
    is_valid_weapon_code()