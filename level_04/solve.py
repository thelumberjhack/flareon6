from itertools import permutations
from scapy.all import *

def valid_moves(piece, bsquare, esquare):
    letmin = ord(a)
    letmax = ord(h)
    nummin = 1
    nummax = 8
    blet = ord(bsquare[0])
    bnum = int(bsquare[1])
    elet = ord(esquare[0])
    enum = int(esquare[1])

    if elet >= letmin and elet <=letmax and enum >= nummin and enum <= nummax:
        if piece == 'king':
            return (
                elet == blet + 1
                or elet == blet - 1
                or enum == bnum + 1
                or enum == bnum -1
            )

        elif piece == 'rook':
            return (
                
            )


def is_valid(ip_addr, nb_moves):
    return ip_addr[0] != 127 or ip_addr[3] & 1 or nb_moves != (ip_addr[2] & 0xf)

def decode_flag(flag, xored_flag, nb_moves, address):
    flag[2*nb_moves] = address[1] ^ xored_flag[2 * nb_moves]
    flag[2*nb_moves + 1] = address[1] ^ xored_flag[2 * nb_moves + 1]

pcaps = rdpcap('./capture.pcap')

replies = []
for p, i in zip(pcaps, range(len(pcaps))):
    if (i%2): # replies
        replies.append(p)
nb_moves = 0
flag = [int(c, 16) for c in "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 40 66 6c 61 72 65 2d 6f 6e 2e 63 6f 6d 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0".split()]
xored_flag = [int(c, 16) for c in "79 5a b8 bc ec d3 df dd 99 a5 b6 ac 15 36 85 8d 09 08 77 52 4d a7 a7 08 16 fd d7 41 20 66".split()]
    
for p in replies:
    ip_addr = p[DNS][DNSRR].rdata.split('.')
    chess_move = p[DNS][DNSRR].rrname.decode().split('.', 1)[0].split('-')
    ip_block = [int(block) for block in ip_addr]
    
    try:
        if is_valid(ip_block, nb_moves):
            decode_flag(flag, xored_flag, nb_moves, ip_block)
            
        nb_moves += 1

    except Exception as e:
        break

print("".join(chr(c) for c in flag))
