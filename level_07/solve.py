import struct
import hashlib
import os
from ctypes import *
from z3 import *

def wrong():
    wopr_pe = None
    with open(os.path.abspath('./wopr/wopr.dump'), 'rb') as f:
        wopr_pe = f.read()

    pe_header = wopr_pe[:1024]

    pe_hdr_offset, = struct.unpack_from('=I', pe_header, 0x3c)

    _, _, numberOfSections, _, _, _, sizeOfOptionalHeader, _ =  struct.unpack_from('=IHHIIIHH', pe_header, pe_hdr_offset)
    assert sizeOfOptionalHeader >= 144

    addressOfEntryPoint, = struct.unpack_from('=I', pe_header, pe_hdr_offset + 40)
    for section_number in range(numberOfSections):
        section_name, virtualSize, virtualAddress, sizeOfRawData, pointerToRawData, _, _, _, _, _ = struct.unpack_from('=8sIIIIIIHHI', pe_header, 40 * section_number + pe_hdr_offset + sizeOfOptionalHeader + 24)
        if virtualAddress <= addressOfEntryPoint < virtualAddress + virtualSize:
            print(section_name)
            break
        # if pointerToRawData <= addressOfEntryPoint < pointerToRawData + sizeOfRawData:
        #     # text section
        #     break

    # spare = wopr_pe[virtualAddress:virtualAddress+virtualSize]
    # text_section = bytearray(wopr_pe[pointerToRawData:pointerToRawData+sizeOfRawData])
    text_section = bytearray(wopr_pe[virtualAddress:virtualAddress+virtualSize])
    
    base_reloc_table_virtAddr, base_reloc_table_size = struct.unpack_from(
        '=II', pe_header, pe_hdr_offset + 0xa0
    )

    reloc_table = wopr_pe[base_reloc_table_virtAddr:base_reloc_table_virtAddr+base_reloc_table_size]
    # with open(os.path.abspath('./wopr/reloc.bin'), 'rb') as f:
    #     reloc_table = f.read()

    reloc_block_addr = 0
    while reloc_block_addr <= len(reloc_table) - 8:
        page_rva, block_size = struct.unpack_from('=II', reloc_table, reloc_block_addr)

        if page_rva == 0 and block_size == 0:
            break

        reloc_block = reloc_table[reloc_block_addr + 8:reloc_block_addr + block_size]

        for i in range(len(reloc_block) >> 1):
            entry, = struct.unpack_from('=H', reloc_block, 2 * i)
            reloc_type = entry >> 12
            if reloc_type != 3:
                # IMAGE_REL_BASED_HIGHLOW
                # The base relocation applies all 32 bits of the difference to the 32-bit field at offset.
                continue
            offset = entry & 4095
            ready = page_rva + offset - virtualAddress
            # ready = page_rva + offset - pointerToRawData
            if 0 <= ready < len(text_section):
                struct.pack_into('=I', text_section, ready, struct.unpack_from('=I', text_section, ready)[0] - 0xc40000)

        reloc_block_addr += block_size

    return hashlib.md5(text_section).digest()

xor = [212, 162, 242, 218, 101, 109, 50, 31, 125, 112, 249, 83, 55, 187, 131, 206]
h = list(wrong())
h = [h[i] ^ xor[i] for i in range(16)]

x = IntVector('x', 16)

s = Solver()

s.add(Int2BV(x[2],8) ^  Int2BV(x[3],8) ^  Int2BV(x[4],8) ^  Int2BV(x[8],8) ^  Int2BV(x[11],8) ^  Int2BV(x[14],8) ==  h[0])
s.add(Int2BV(x[0],8) ^  Int2BV(x[1],8) ^  Int2BV(x[8],8) ^  Int2BV(x[11],8) ^  Int2BV(x[13],8) ^  Int2BV(x[14],8) ==  h[1])
s.add(Int2BV(x[0],8) ^  Int2BV(x[1],8) ^  Int2BV(x[2],8) ^  Int2BV(x[4],8) ^  Int2BV(x[5],8) ^  Int2BV(x[8],8) ^  Int2BV(x[9],8) ^  Int2BV(x[10],8) ^  Int2BV(x[13],8) ^  Int2BV(x[14],8) ^  Int2BV(x[15],8) ==  h[2])
s.add(Int2BV(x[5],8) ^  Int2BV(x[6],8) ^  Int2BV(x[8],8) ^  Int2BV(x[9],8) ^  Int2BV(x[10],8) ^  Int2BV(x[12],8) ^  Int2BV(x[15],8) ==  h[3])
s.add(Int2BV(x[1],8) ^  Int2BV(x[6],8) ^  Int2BV(x[7],8) ^  Int2BV(x[8],8) ^  Int2BV(x[12],8) ^  Int2BV(x[13],8) ^  Int2BV(x[14],8) ^  Int2BV(x[15],8) ==  h[4])
s.add(Int2BV(x[0],8) ^  Int2BV(x[4],8) ^  Int2BV(x[7],8) ^  Int2BV(x[8],8) ^  Int2BV(x[9],8) ^  Int2BV(x[10],8) ^  Int2BV(x[12],8) ^  Int2BV(x[13],8) ^  Int2BV(x[14],8) ^  Int2BV(x[15],8) ==  h[5])
s.add(Int2BV(x[1],8) ^  Int2BV(x[3],8) ^  Int2BV(x[7],8) ^  Int2BV(x[9],8) ^  Int2BV(x[10],8) ^  Int2BV(x[11],8) ^  Int2BV(x[12],8) ^  Int2BV(x[13],8) ^  Int2BV(x[15],8) ==  h[6])
s.add(Int2BV(x[0],8) ^  Int2BV(x[1],8) ^  Int2BV(x[2],8) ^  Int2BV(x[3],8) ^  Int2BV(x[4],8) ^  Int2BV(x[8],8) ^  Int2BV(x[10],8) ^  Int2BV(x[11],8) ^  Int2BV(x[14],8) ==  h[7])
s.add(Int2BV(x[1],8) ^  Int2BV(x[2],8) ^  Int2BV(x[3],8) ^  Int2BV(x[5],8) ^  Int2BV(x[9],8) ^  Int2BV(x[10],8) ^  Int2BV(x[11],8) ^  Int2BV(x[12],8) ==  h[8])
s.add(Int2BV(x[6],8) ^  Int2BV(x[7],8) ^  Int2BV(x[8],8) ^  Int2BV(x[10],8) ^  Int2BV(x[11],8) ^  Int2BV(x[12],8) ^  Int2BV(x[15],8) ==  h[9])
s.add(Int2BV(x[0],8) ^  Int2BV(x[3],8) ^  Int2BV(x[4],8) ^  Int2BV(x[7],8) ^  Int2BV(x[8],8) ^  Int2BV(x[10],8) ^  Int2BV(x[11],8) ^  Int2BV(x[12],8) ^  Int2BV(x[13],8) ^  Int2BV(x[14],8) ^  Int2BV(x[15],8) ==  h[10])
s.add(Int2BV(x[0],8) ^  Int2BV(x[2],8) ^  Int2BV(x[4],8) ^  Int2BV(x[6],8) ^  Int2BV(x[13],8) ==  h[11])
s.add(Int2BV(x[0],8) ^  Int2BV(x[3],8) ^  Int2BV(x[6],8) ^  Int2BV(x[7],8) ^  Int2BV(x[10],8) ^  Int2BV(x[12],8) ^  Int2BV(x[15],8) ==  h[12])
s.add(Int2BV(x[2],8) ^  Int2BV(x[3],8) ^  Int2BV(x[4],8) ^  Int2BV(x[5],8) ^  Int2BV(x[6],8) ^  Int2BV(x[7],8) ^  Int2BV(x[11],8) ^  Int2BV(x[12],8) ^  Int2BV(x[13],8) ^  Int2BV(x[14],8) ==  h[13])
s.add(Int2BV(x[1],8) ^  Int2BV(x[2],8) ^  Int2BV(x[3],8) ^  Int2BV(x[5],8) ^  Int2BV(x[7],8) ^  Int2BV(x[11],8) ^  Int2BV(x[13],8) ^  Int2BV(x[14],8) ^  Int2BV(x[15],8) ==  h[14])
s.add(Int2BV(x[1],8) ^  Int2BV(x[3],8) ^  Int2BV(x[5],8) ^  Int2BV(x[9],8) ^  Int2BV(x[10],8) ^  Int2BV(x[11],8) ^  Int2BV(x[13],8) ^  Int2BV(x[15],8) ==  h[15])

s.add(
    0 < x[0], x[0] < 256,
    0 < x[1], x[1] < 256,
    0 < x[2], x[2] < 256,
    0 < x[3], x[3] < 256,
    0 < x[4], x[4] < 256,
    0 < x[5], x[5] < 256,
    0 < x[6], x[6] < 256,
    0 < x[7], x[7] < 256,
    0 < x[8], x[8] < 256,
    0 < x[9], x[9] < 256,
    0 < x[10], x[10] < 256,
    0 < x[11], x[11] < 256,
    0 < x[12], x[12] < 256,
    0 < x[13], x[13] < 256,
    0 < x[14], x[14] < 256,
    0 < x[15], x[15] < 256,
)

results = []

while True:
    if s.check() == sat:
        launch_code = s.model()
        print(launch_code)
        results.append(launch_code)
        block = []

        for d in launch_code:
            c = d()
            block.append(c != launch_code[d])

        s.add(Or(block))

    else:
        print(f"Total results: {len(results)}")
        break

launch = "".join([chr(c) for c in [53, 67, 48, 71, 55, 84, 89, 50, 76, 87, 73, 50, 89, 88, 77, 66]])

print(launch) # 5C0G7TY2LWI2YXMB
# L1n34R_4L93bR4_i5_FuN@flare-on.com