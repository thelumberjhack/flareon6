# BMPhide

## Message

> Tyler Dean hiked up Mt. Elbert (Colorado's tallest mountain) at 2am to capture this picture at the perfect time.
> Never skip leg day. We found this picture and executable on a thumb drive he left at the trail head. Can he be trusted?

## First look

This is a dotnet PE and judging by the name and message, it's probably a steganography level. So the first idea that
comes to my mind was the LSB technique. But it's level 6 so that would be too easy.

It appears that it's more complicated than this.

## Analysis

After opening it in ILSpy we are confronted with 3 classes: A, D and Program.

The main function is in the Program class and is as follows:

```C#
private static void Main(string[] args)
{
    Init();
    yy += 18;                                       // yy = 2738, ww = "1F7D1482", zz = "MzQxOTk="
    string filename = args[2]; 						// output
    string fullPath = Path.GetFullPath(args[0]);	// image to hide data in
    string fullPath2 = Path.GetFullPath(args[1]);	// data to hide
    byte[] data = File.ReadAllBytes(fullPath2);		// gets the data to be hidden
    Bitmap bitmap = new Bitmap(fullPath);			// reads orig BMP data
    byte[] data2 = h(data);							// shuffle data
    i(bitmap, data2);								// hide it in BMP
    bitmap.Save(filename);							// saves it to new BMP
}
```

## Debugging dotNET in WinDBG preview

### TTD

```shell
C:\flareon6\level_06\bmphide>%programdata%\Microsoft\Windbg\1-1906-12001\TTD\TTD.exe -launch bmphide.exe Luigi2.bmp tohide.bmp out.bmp
Microsoft (R) TTD 1.01.05
Release: 10.0.18914.1001
Copyright (C) Microsoft Corporation. All rights reserved.

Launching bmphide.exe Luigi2.bmp tohide.bmp out.bmp

bmphide.exe(x86) (PID:12268): Process exited with exit code 0 after 6640ms
  Full trace dumped to C:\flareon6\level_06\bmphide\bmphide01.run
```

```
0:000> !dumpmt -md 00e84f0c
EEClass:         00e81820
Module:          00e8403c
Name:            BMPHIDE.Program
mdToken:         02000004
File:            C:\flareon6\level_06\bmphide\bmphide.exe
BaseSize:        0xc
ComponentSize:   0x0
Slots in VTable: 18
Number of IFaces in IFaceMap: 0
--------------------------------------
MethodDesc Table
   Entry MethodDe    JIT Name
6d9e87b8 6d5ec838 PreJIT System.Object.ToString()
6d9e86a0 6d728568 PreJIT System.Object.Equals(System.Object)
6d9f1140 6d728588 PreJIT System.Object.GetHashCode()
6d9a3f2c 6d728590 PreJIT System.Object.Finalize()
02630848 00e84f04    JIT BMPHIDE.Program..cctor()
02630469 00e84efc   NONE BMPHIDE.Program..ctor()
026309b8 00e84e6c    JIT BMPHIDE.Program.Init()
02633210 00e84e84    JIT BMPHIDE.Program.b(Byte, Int32)
02633210 00e84e84    JIT BMPHIDE.Program.b(Byte, Int32)
02633380 00e84e9c    JIT BMPHIDE.Program.d(Byte, Int32)
02633380 00e84e9c    JIT BMPHIDE.Program.d(Byte, Int32)
0263044d 00e84ea8   NONE BMPHIDE.Program.e(Byte, Byte)
02630451 00e84eb4   NONE BMPHIDE.Program.f(Int32)
02630455 00e84ec0   NONE BMPHIDE.Program.g(Int32)
02630459 00e84ecc   NONE BMPHIDE.Program.h(Byte[])
0263045d 00e84ed8   NONE BMPHIDE.Program.i(System.Drawing.Bitmap, Byte[])
02630461 00e84ee4   NONE BMPHIDE.Program.j(Byte)
02630898 00e84ef0    JIT BMPHIDE.Program.Main(System.String[])
```

### Useful Windbg + .Net commands

```
sxe ld clrjit; g
.loadby sos clr
.load sosex
!name2ee bmphide BMPHIDE.Program
!dumpmt -md <method_table_addr>
!bpmd bmphide BMPHIDE.Program.Main
```

## Flag

`dOnT_tRu$t_vEr1fy@flare-on.com`

## Resources

- [.NET Internals and Code Injection](https://www.codeproject.com/articles/26060/net-internals-and-code-injection)
- [Getting the disassembly and IL for a Jitted\NGENed .Net method using WinDbg and SOS.dll](https://dpaoliello.wordpress.com/2012/02/05/getting-the-disassembly-and-il-for-a-jittedngened-net-method-using-windbg-and-sos-dll/)
- [Setting up managed code debugging (with SOS and SOSEX)](https://blogs.msdn.microsoft.com/jankrivanek/2012/11/15/setting-up-managed-code-debugging-with-sos-and-sosex/)
- [Debugging managed code](https://docs.microsoft.com/en-us/windows-hardware/drivers/debugger/debugging-managed-code)
- [SOS.dll (SOS debugging extension)](https://docs.microsoft.com/en-us/dotnet/framework/tools/sos-dll-sos-debugging-extension)
- [Debugging .NET Apps with Time Travel Debugging (TTD)](https://devblogs.microsoft.com/dotnet/debugging-net-apps-with-time-travel-debugging-ttd/)
