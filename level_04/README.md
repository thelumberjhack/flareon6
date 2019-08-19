# Dnschess

## Message

> Some suspicious network traffic led us to this unauthorized chess program running on an Ubuntu desktop.
> This appears to be the work of cyberspace computer hackers. You'll need to make the right moves to solve this one. Good luck!

## Init

ChessUI, pcap containing dns requests with chess pieces positions. 1st guess: play chess using same positions to unlock the flag.

## Playing
Extracted all the positions from the pcap:

```python
from scapy.all import *

pcaps = rdpcap('./capture.pcap')

replies = []

for pkt in pcaps:
    if pkt[DNS].aa:
        print(pkt[DNSRR].rrname.decode().split('.', 1)[0])
```

```shell
rook-c3-c6
knight-g1-f3
pawn-c2-c4
knight-c7-d5
bishop-f1-e2
rook-a1-g1
bishop-c1-f4
bishop-c6-a8
pawn-e2-e4
king-g1-h1
knight-g1-h3
king-e5-f5
queen-d1-f3
pawn-e5-e6
king-c4-b3
king-c1-b1
queen-d1-h5
bishop-f3-c6
knight-d2-c4
pawn-c6-c7
bishop-f4-g3
rook-d3-e3
pawn-e4-e5
queen-a8-g2
queen-a3-b4
queen-h5-f7
pawn-h4-h5
bishop-e2-f3
pawn-g2-g3
knight-h8-g6
bishop-b3-f7
queen-d1-d6
knight-b1-c3
bishop-f1-d3
rook-b4-h4
bishop-c1-a3
bishop-e8-b5
rook-f2-f3
pawn-a2-a4
pawn-d2-d4
```

Sets a /etc/hosts file containing all resolutions and play the game. Order and legality of the moves matter, and here is the list:

```
p d2 d4
p c2 c4
k b1 c3
p e2 e4
k g1 f3
b c1 f4
b f1 e2
b e2 f3
b f4 g3
p e4 e5
b f3 c6
b c6 a8
p e5 e6
q d1 h5
q h5 f7
```

Total of 15 moves.

## Reversing and scripting

TBD

## Flag

LooksLikeYouLockedUpTheLookupZ@flare-on.com