# WOPR

## Message

> We used our own computer hacking skills to "find" this AI on a military supercomputer. It does
> strongly resemble the classic 1983 movie WarGames. Perhaps life imitates art? If you can find
> the launch codes for us, we'll let you pass to the next challenge. We promise not to start a
> thermonuclear war.

## Solving

This EXE is a [pyinstaller]() executable. This means that we can get original python source
code from it by using appropriate tools:

- [pyinstxtractor.py](https://sourceforge.net/projects/pyinstallerextractor/)
- [uncompyle6](https://github.com/rocky/python-uncompyle6)

Using pyinstxtractor on wopr.exe, we're given the following output:

```shell
python pyinstxtractor.py wopr/wopr.exe 
pyinstxtractor.py:86: DeprecationWarning: the imp module is deprecated in favour of importlib; see the module's documentation for alternative uses
  import imp
[*] Processing wopr/wopr.exe
[*] Pyinstaller version: 2.1+
[*] Python version: 37
[*] Length of package: 5068358 bytes
[*] Found 64 files in CArchive
[*] Beginning extraction...please standby
[+] Possible entry point: pyiboot01_bootstrap
[+] Possible entry point: pyiboot02_cleanup
[*] Found 135 files in PYZ archive
[*] Successfully extracted pyinstaller archive: wopr/wopr.exe

You can now use a python decompiler on the pyc files within the extracted directory
```

We can see those 2 lines:

```shell
[+] Possible entry point: pyiboot01_bootstrap
[+] Possible entry point: pyiboot02_cleanup
```

Let's try to decompile them with uncompyle6:

```shell
uncompyle6 wopr.exe_extracted/pyiboot01_bootstrap 


# file wopr.exe_extracted/pyiboot01_bootstrap
# path wopr.exe_extracted/pyiboot01_bootstrap must point to a .py or .pyc file
```

It fails because the files need to have the extension pyc or py to work. Here we go again:

```shell
cp wopr.exe_extracted/pyiboot01_bootstrap wopr.exe_extracted/pyiboot01_bootstrap.pyc

uncompyle6 wopr.exe_extracted/pyiboot01_bootstrap.pyc 
Traceback (most recent call last):
  File "/home/yannick/.local/share/virtualenvs/flareon6-92oRDxKM/lib/python3.7/site-packages/xdis/load.py", line 143, in load_module_from_file_object
    float_version = float(magics.versions[magic][:3])
KeyError: b'\xe3\x00\x00\x00'

During handling of the above exception, another exception occurred:

Traceback (most recent call last):
  File "/home/yannick/.local/share/virtualenvs/flareon6-92oRDxKM/bin/uncompyle6", line 10, in <module>
    sys.exit(main_bin())
  File "/home/yannick/.local/share/virtualenvs/flareon6-92oRDxKM/lib/python3.7/site-packages/uncompyle6/bin/uncompile.py", line 194, in main_bin
    **options)
  File "/home/yannick/.local/share/virtualenvs/flareon6-92oRDxKM/lib/python3.7/site-packages/uncompyle6/main.py", line 261, in main
    source_encoding, linemap_stream, do_fragments)
  File "/home/yannick/.local/share/virtualenvs/flareon6-92oRDxKM/lib/python3.7/site-packages/uncompyle6/main.py", line 161, in decompile_file
    source_size) = load_module(filename, code_objects)
  File "/home/yannick/.local/share/virtualenvs/flareon6-92oRDxKM/lib/python3.7/site-packages/xdis/load.py", line 116, in load_module
    get_code=get_code,
  File "/home/yannick/.local/share/virtualenvs/flareon6-92oRDxKM/lib/python3.7/site-packages/xdis/load.py", line 152, in load_module_from_file_object
    % (ord(magic[0:1]) + 256 * ord(magic[1:2]), filename)
ImportError: Unknown magic number 227 in wopr.exe_extracted/pyiboot01_bootstrap.pyc
```

Oopsie, still not OK. This error means that we might need to add the header with the magic number
indicating what version of python bytecode was used. Looking at other files in the extracted
python compiled codes, we can see the differences between our previous attempt. So let's add
those 16 bytes:

```shell
>>> header = "42 0d 0d 0a 00 00 00 00 70 79 69 30 10 01 00 00".split()                                  

>>> hdr = bytearray([int(c,16) for c in header])                                                        

>>> orig = None 
... with open('./wopr.exe_extracted/pyiboot01_bootstrap', 'rb') as f: 
...     orig = f.read() 
... with open('./boostrap.pyc', 'wb') as fh: 
...     fh.write(hdr+orig)                                                                              

>>> orig = None 
... with open('./wopr.exe_extracted/pyiboot02_cleanup', 'rb') as f: 
...     orig = f.read() 
... with open('./cleanup.pyc', 'wb') as fh: 
...     fh.write(hdr+orig)
```

And this time uncompyle6 is able to give us the original source code [cleanup]('./cleanup.py').

### The Raven

Uncompyle replaces tabs with spaces at the end of the docstrings line so we need to extract
docstrings from the pyc directly:

```shell
>>> start = 0x16d

>>> end = 0x8f94

>>> hex(end -start)       
'0x8e27'

>>> docstring = None       

>>> with open('./cleanup.py', 'rb') as f:
...     code  = f.read()
...     with open('./cleanup2.py', 'wb') as h:
...         h.write(docstring + code)
```

### stage 2

```shell
>>> xor = [212, 162, 242, 218, 101, 109, 50, 31, 125, 112, 249, 83, 55, 187, 131, 206]

>>> h = [30, 254, 225, 26, 229, 131, 35, 120, 29, 18, 211, 112, 227, 1, 101, 119]
```

## Resources

- [Solving a PyInstaller-compiled crackme](https://hshrzd.wordpress.com/2018/01/26/solving-a-pyinstaller-compiled-crackme/)
- [Hack.lu 2012 - Dropbox Security](http://archive.hack.lu/2012/Dropbox%20security.pdf)
