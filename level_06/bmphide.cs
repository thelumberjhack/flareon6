// BMPHIDE.Program
using BMPHIDE;
using System;
using System.Drawing;
using System.IO;
using System.Reflection;
using System.Runtime.CompilerServices;
using System.Text;
using System.Runtime.InteropServices;
using System.Collections.Generic;
using System.Linq;

public class A
{
	[StructLayout(LayoutKind.Sequential, Size = 24)]
	private struct CORINFO_EH_CLAUSE
	{
	}

	[StructLayout(LayoutKind.Sequential, Pack = 1)]
	private struct CORINFO_METHOD_INFO
	{
		public IntPtr ftn;

		public IntPtr scope;

		public unsafe byte* ILCode;

		public uint ILCodeSize;
	}

	private struct CORINFO_SIG_INFO_x64
	{
		public uint callConv;

		private uint pad1;

		public IntPtr retTypeClass;

		public IntPtr retTypeSigClass;

		public byte retType;

		public byte flags;

		public ushort numArgs;

		private uint pad2;

		public CORINFO_SIG_INST_x64 sigInst;

		public IntPtr args;

		public IntPtr sig;

		public IntPtr scope;

		public uint token;

		private uint pad3;
	}

	private struct CORINFO_SIG_INFO_x86
	{
		public uint callConv;

		public IntPtr retTypeClass;

		public IntPtr retTypeSigClass;

		public byte retType;

		public byte flags;

		public ushort numArgs;

		public CORINFO_SIG_INST_x86 sigInst;

		public IntPtr args;

		public IntPtr sig;

		public IntPtr scope;

		public uint token;
	}

	[StructLayout(LayoutKind.Sequential, Size = 32)]
	private struct CORINFO_SIG_INST_x64
	{
	}

	[StructLayout(LayoutKind.Sequential, Size = 16)]
	private struct CORINFO_SIG_INST_x86
	{
	}

	private struct ICorClassInfo
	{
		public unsafe readonly IntPtr* vfptr;
	}

	private struct ICorDynamicInfo
	{
		public unsafe IntPtr* vfptr;

		public unsafe int* vbptr;

		public unsafe static ICorStaticInfo* ICorStaticInfo(ICorDynamicInfo* ptr)
		{
			return (ICorStaticInfo*)((byte*)(&ptr->vbptr) + ptr->vbptr[hasLinkInfo ? 9 : 8]);
		}
	}

	private struct ICorJitInfo
	{
		public unsafe IntPtr* vfptr;

		public unsafe int* vbptr;

		public unsafe static ICorDynamicInfo* ICorDynamicInfo(ICorJitInfo* ptr)
		{
			hasLinkInfo = (ptr->vbptr[10] > 0 && ptr->vbptr[10] >> 16 == 0);
			return (ICorDynamicInfo*)((byte*)(&ptr->vbptr) + ptr->vbptr[hasLinkInfo ? 10 : 9]);
		}
	}

	private struct ICorMethodInfo
	{
		public unsafe IntPtr* vfptr;
	}

	private struct ICorModuleInfo
	{
		public unsafe IntPtr* vfptr;
	}

	private struct ICorStaticInfo
	{
		public unsafe IntPtr* vfptr;

		public unsafe int* vbptr;

		public unsafe static ICorMethodInfo* ICorMethodInfo(ICorStaticInfo* ptr)
		{
			return (ICorMethodInfo*)((byte*)(&ptr->vbptr) + ptr->vbptr[1]);
		}

		public unsafe static ICorModuleInfo* ICorModuleInfo(ICorStaticInfo* ptr)
		{
			return (ICorModuleInfo*)((byte*)(&ptr->vbptr) + ptr->vbptr[2]);
		}

		public unsafe static ICorClassInfo* ICorClassInfo(ICorStaticInfo* ptr)
		{
			return (ICorClassInfo*)((byte*)(&ptr->vbptr) + ptr->vbptr[3]);
		}
	}

	private class CorMethodInfoHook
	{
		private static int ehNum = -1;

		public unsafe CORINFO_EH_CLAUSE* clauses;

		public IntPtr ftn;

		public unsafe ICorMethodInfo* info;

		public getEHinfo n_getEHinfo;

		public unsafe IntPtr* newVfTbl;

		public getEHinfo o_getEHinfo;

		public unsafe IntPtr* oldVfTbl;

		private unsafe void hookEHInfo(IntPtr self, IntPtr ftn, uint EHnumber, CORINFO_EH_CLAUSE* clause)
		{
			if (ftn == this.ftn)
			{
				*clause = clauses[EHnumber];
			}
			else
			{
				o_getEHinfo(self, ftn, EHnumber, clause);
			}
		}

		public unsafe void Dispose()
		{
			Marshal.FreeHGlobal((IntPtr)(void*)newVfTbl);
			info->vfptr = oldVfTbl;
		}

		public unsafe static CorMethodInfoHook Hook(ICorJitInfo* comp, IntPtr ftn, CORINFO_EH_CLAUSE* clauses)
		{
			ICorMethodInfo* ptr = ICorStaticInfo.ICorMethodInfo(ICorDynamicInfo.ICorStaticInfo(ICorJitInfo.ICorDynamicInfo(comp)));
			IntPtr* vfptr = ptr->vfptr;
			IntPtr* ptr2 = (IntPtr*)(void*)Marshal.AllocHGlobal(27 * IntPtr.Size);
			for (int i = 0; i < 27; i++)
			{
				ptr2[i] = vfptr[i];
			}
			if (ehNum == -1)
			{
				for (int j = 0; j < 27; j++)
				{
					bool flag = true;
					for (byte* ptr3 = (byte*)(void*)vfptr[j]; *ptr3 != 233; ptr3++)
					{
						if ((IntPtr.Size != 8) ? (*ptr3 == 131 && ptr3[1] == 233) : (*ptr3 == 72 && ptr3[1] == 129 && ptr3[2] == 233))
						{
							flag = false;
							break;
						}
					}
					if (flag)
					{
						ehNum = j;
						break;
					}
				}
			}
			CorMethodInfoHook corMethodInfoHook = new CorMethodInfoHook
			{
				ftn = ftn,
				info = ptr,
				clauses = clauses,
				newVfTbl = ptr2,
				oldVfTbl = vfptr
			};
			corMethodInfoHook.n_getEHinfo = corMethodInfoHook.hookEHInfo;
			corMethodInfoHook.o_getEHinfo = (getEHinfo)Marshal.GetDelegateForFunctionPointer(vfptr[ehNum], typeof(getEHinfo));
			ptr2[ehNum] = Marshal.GetFunctionPointerForDelegate(corMethodInfoHook.n_getEHinfo);
			ptr->vfptr = ptr2;
			return corMethodInfoHook;
		}
	}

	private class CorJitInfoHook
	{
		public unsafe CORINFO_EH_CLAUSE* clauses;

		public IntPtr ftn;

		public unsafe ICorJitInfo* info;

		public getEHinfo n_getEHinfo;

		public unsafe IntPtr* newVfTbl;

		public getEHinfo o_getEHinfo;

		public unsafe IntPtr* oldVfTbl;

		private unsafe void hookEHInfo(IntPtr self, IntPtr ftn, uint EHnumber, CORINFO_EH_CLAUSE* clause)
		{
			if (ftn == this.ftn)
			{
				*clause = clauses[EHnumber];
			}
			else
			{
				o_getEHinfo(self, ftn, EHnumber, clause);
			}
		}

		public unsafe void Dispose()
		{
			Marshal.FreeHGlobal((IntPtr)(void*)newVfTbl);
			info->vfptr = oldVfTbl;
		}

		public unsafe static CorJitInfoHook Hook(ICorJitInfo* comp, IntPtr ftn, CORINFO_EH_CLAUSE* clauses)
		{
			IntPtr* vfptr = comp->vfptr;
			IntPtr* ptr = (IntPtr*)(void*)Marshal.AllocHGlobal(158 * IntPtr.Size);
			for (int i = 0; i < 158; i++)
			{
				ptr[i] = vfptr[i];
			}
			CorJitInfoHook corJitInfoHook = new CorJitInfoHook
			{
				ftn = ftn,
				info = comp,
				clauses = clauses,
				newVfTbl = ptr,
				oldVfTbl = vfptr
			};
			corJitInfoHook.n_getEHinfo = corJitInfoHook.hookEHInfo;
			corJitInfoHook.o_getEHinfo = (getEHinfo)Marshal.GetDelegateForFunctionPointer(vfptr[8], typeof(getEHinfo));
			ptr[8] = Marshal.GetFunctionPointerForDelegate(corJitInfoHook.n_getEHinfo);
			comp->vfptr = ptr;
			return corJitInfoHook;
		}
	}

	private struct MethodData
	{
		public readonly uint ILCodeSize;

		public readonly uint MaxStack;

		public readonly uint EHCount;

		public readonly uint LocalVars;

		public readonly uint Options;

		public readonly uint MulSeed;
	}

	[UnmanagedFunctionPointer(CallingConvention.StdCall)]
	private unsafe delegate uint locateNativeCallingConvention(IntPtr self, ICorJitInfo* comp, CORINFO_METHOD_INFO* info, uint flags, byte** nativeEntry, uint* nativeSizeOfCode);

	[UnmanagedFunctionPointer(CallingConvention.ThisCall)]
	private unsafe delegate void getEHinfo(IntPtr self, IntPtr ftn, uint EHnumber, CORINFO_EH_CLAUSE* clause);

	private unsafe delegate IntPtr* getJit();

	[UnmanagedFunctionPointer(CallingConvention.ThisCall)]
	private delegate uint getMethodDefFromMethod(IntPtr self, IntPtr ftn);

	private static locateNativeCallingConvention originalDelegate;

	private static bool ver4;

	private static bool ver5;

	private static locateNativeCallingConvention handler;

	private static bool hasLinkInfo;

	public static void CalculateStack()
	{
		Module module = typeof(A).Module;
		ModuleHandle moduleHandle = module.ModuleHandle;
		ver4 = (Environment.Version.Major == 4);
		if (ver4)
		{
			ver5 = (Environment.Version.Revision > 17020);
		}
		IdentifyLocals();
	}

	[DllImport("kernel32.dll")]
	private static extern IntPtr LoadLibrary(string lib);

	[DllImport("kernel32.dll")]
	private static extern IntPtr GetProcAddress(IntPtr lib, string proc);

	[DllImport("kernel32.dll")]
	private static extern bool VirtualProtect(IntPtr lpAddress, uint dwSize, uint flNewProtect, out uint lpflOldProtect);

	private unsafe static void IdentifyLocals()
	{
		ulong* ptr = stackalloc ulong[2];
		if (ver4)
		{
			*ptr = 7218835248827755619uL;	// 'clrjit.d'
			ptr[1] = 27756uL;				// 'll'
		}
		else
		{
			*ptr = 8388352820681864045uL;	// 'mscorjit'
			ptr[1] = 1819042862uL;			// '.dll'
		}
		IntPtr lib = LoadLibrary(new string((sbyte*)ptr));
		*ptr = 127995569530215uL; 				// 'getJit'
		getJit getJit = (getJit)Marshal.GetDelegateForFunctionPointer(GetProcAddress(lib, new string((sbyte*)ptr)), typeof(getJit));
		IntPtr intPtr = *getJit();
		IntPtr val = *(IntPtr*)(void*)intPtr;
		IntPtr intPtr2;												// Unmanaged function pointer
		uint lpflOldProtect;
		if (IntPtr.Size == 8) 										// x64
		{
			intPtr2 = Marshal.AllocHGlobal(16);
			ulong* ptr2 = (ulong*)(void*)intPtr2;
			*ptr2 = 18446744073709533256uL;							// '48 b8 ff ff ff ff ff ff'
			ptr2[1] = 10416984890032521215uL;						// 'ff ff ff e0 90 90 90 90'
			VirtualProtect(intPtr2, 12u, 64u, out lpflOldProtect);	// 12 bytes of PAGE_READWRITE_EXECUTE mem
			Marshal.WriteIntPtr(intPtr2, 2, val);					// Writes val at offset 2 of intPtr2 address
		}
		else
		{
			intPtr2 = Marshal.AllocHGlobal(8);						// x86
			ulong* ptr3 = (ulong*)(void*)intPtr2;
			*ptr3 = 10439625411221520312uL;							// 'b8 ff ff ff ff ff e0 90'
			VirtualProtect(intPtr2, 7u, 64u, out lpflOldProtect);	// 7 bytes of PAGE_READWRITE_EXECUTE mem
			Marshal.WriteIntPtr(intPtr2, 1, val);					// 
		}
		//Converts an unmanaged function pointer to a delegate.
		originalDelegate = (locateNativeCallingConvention)Marshal.GetDelegateForFunctionPointer(intPtr2, typeof(locateNativeCallingConvention));
		handler = IncrementMaxStack;
		// Indicates that the specified delegate should be prepared for inclusion in a constrained execution region (CER).
		RuntimeHelpers.PrepareDelegate(originalDelegate);
		RuntimeHelpers.PrepareDelegate(handler);
		VirtualProtect(intPtr, (uint)IntPtr.Size, 64u, out lpflOldProtect);
		Marshal.WriteIntPtr(intPtr, Marshal.GetFunctionPointerForDelegate(handler));
		VirtualProtect(intPtr, (uint)IntPtr.Size, lpflOldProtect, out lpflOldProtect);
	}

	public static MethodBase c(IntPtr MethodHandleValue)
	{
		Type[] types = Assembly.GetExecutingAssembly().GetTypes();
		foreach (Type type in types)
		{
			MethodInfo[] methods = type.GetMethods(BindingFlags.DeclaredOnly | BindingFlags.Instance | BindingFlags.Static | BindingFlags.Public | BindingFlags.NonPublic);
			MethodInfo[] array = methods;
			foreach (MethodInfo methodInfo in array)
			{
				if (methodInfo.MethodHandle.Value == MethodHandleValue)
				{
					return methodInfo;
				}
			}
			ConstructorInfo[] constructors = type.GetConstructors(BindingFlags.DeclaredOnly | BindingFlags.Instance | BindingFlags.Static | BindingFlags.Public | BindingFlags.NonPublic);
			ConstructorInfo[] array2 = constructors;
			foreach (ConstructorInfo constructorInfo in array2)
			{
				if (constructorInfo.MethodHandle.Value == MethodHandleValue)
				{
					return constructorInfo;
				}
			}
		}
		return null;
	}

	private unsafe static uint IncrementMaxStack(IntPtr self, ICorJitInfo* comp, CORINFO_METHOD_INFO* info, uint flags, byte** nativeEntry, uint* nativeSizeOfCode)
	{
		if (info != null)
		{
			MethodBase methodBase = c(info->ftn);
			if (methodBase != null)
			{
				if (methodBase.MetadataToken == 100663317)
				{
					VirtualProtect((IntPtr)(void*)info->ILCode, info->ILCodeSize, 4u, out uint lpflOldProtect);
					Marshal.WriteByte((IntPtr)(void*)info->ILCode, 23, 20);
					Marshal.WriteByte((IntPtr)(void*)info->ILCode, 62, 20);
					VirtualProtect((IntPtr)(void*)info->ILCode, info->ILCodeSize, lpflOldProtect, out lpflOldProtect);
				}
				else if (methodBase.MetadataToken == 100663316)
				{
					VirtualProtect((IntPtr)(void*)info->ILCode, info->ILCodeSize, 4u, out uint lpflOldProtect2);
					Marshal.WriteInt32((IntPtr)(void*)info->ILCode, 6, 309030853);
					Marshal.WriteInt32((IntPtr)(void*)info->ILCode, 18, 209897853);
					VirtualProtect((IntPtr)(void*)info->ILCode, info->ILCodeSize, lpflOldProtect2, out lpflOldProtect2);
				}
			}
		}
		return originalDelegate(self, comp, info, flags, nativeEntry, nativeSizeOfCode);
	}

	public unsafe static void VerifySignature(MethodInfo m1, MethodInfo m2)
	{
		RuntimeHelpers.PrepareMethod(m1.MethodHandle);
		RuntimeHelpers.PrepareMethod(m2.MethodHandle);
		int* ptr = (int*)((byte*)m1.MethodHandle.Value.ToPointer() + 2L * 4L);
		int* ptr2 = (int*)((byte*)m2.MethodHandle.Value.ToPointer() + 2L * 4L);
		*ptr = *ptr2;
	}
}


public class D
{
	private const uint s_generator = 1611621881u;

	private readonly uint[] m_checksumTable;

	public D()
	{
		m_checksumTable = Enumerable.Range(0, 256).Select(delegate(int i)
		{
			uint num = (uint)i;
			for (int j = 0; j < 8; j++)
			{
				num = (((num & 1) != 0) ? (0x600F65F9 ^ (num >> 1)) : (num >> 1));
			}
			return num;
		}).ToArray();
	}

	public uint a<T>(IEnumerable<T> byteStream)
	{
		return ~byteStream.Aggregate(uint.MaxValue, (uint checksumRegister, T currentByte) => m_checksumTable[(checksumRegister & 0xFF) ^ Convert.ToByte(currentByte)] ^ (checksumRegister >> 8));
	}
}


internal class Program
{
	public static int yy = 20;

	public static string ww = "1F7D";

	public static string zz = "MTgwMw==";

	private static void Init()
	{
		yy *= 136;							// yy = 136 * 20 = 2720
		Type typeFromHandle = typeof(A);
		ww += "14";							// ww = "1f7d14"
		// searches for the methods defined for the current Type, using the specified binding constraints
		MethodInfo[] methods = typeFromHandle.GetMethods(
			BindingFlags.DeclaredOnly		// Specifies that only members declared at the level of the supplied type's hierarchy should be considered. Inherited members are not considered.
			| BindingFlags.Instance			// Specifies that instance members are to be included in the search.
			| BindingFlags.Static			// Specifies that static members are to be included in the search.
			| BindingFlags.Public 			// Specifies that public members are to be included in the search.
			| BindingFlags.NonPublic		// Specifies that non-public members are to be included in the search.
		);
		MethodInfo[] array = methods;
		foreach (MethodInfo methodInfo in array)
		{
			//Prepares a method for inclusion in a constrained execution region (CER).
			RuntimeHelpers.PrepareMethod(methodInfo.MethodHandle);
		}
		A.CalculateStack();
		ww += "82";							// ww = "1f7d1482"
		MethodInfo m = null;
		MethodInfo m2 = null;
		MethodInfo m3 = null;
		MethodInfo m4 = null;
		zz = "MzQxOTk=";					// zz = '34199'
		MethodInfo[] methods2 = typeof(Program).GetMethods();
		foreach (MethodInfo methodInfo2 in methods2)
		{
			if (methodInfo2.GetMethodBody() == null)
			{
				continue;
			}
			byte[] iLAsByteArray = methodInfo2.GetMethodBody().GetILAsByteArray();
			if (iLAsByteArray.Length > 8)
			{
				byte[] array2 = new byte[iLAsByteArray.Length - 2];
				Buffer.BlockCopy(iLAsByteArray, 2, array2, 0, iLAsByteArray.Length - 2);
				D d = new D();
				switch (d.a(array2))
				{
				case 3472577156u:
					m = methodInfo2;
					break;
				case 2689456752u:
					m2 = methodInfo2;
					break;
				case 3040029055u:
					m3 = methodInfo2;
					break;
				case 2663056498u:
					m4 = methodInfo2;
					break;
				}
			}
		}
		A.VerifySignature(m, m2);
		A.VerifySignature(m3, m4);
	}

	public static byte a(byte b, int r)
	{
		return (byte)(((b + r) ^ r) & 0xFF);
	}

	public static byte b(byte b, int r)
	{
		for (int i = 0; i < r; i++)
		{
			byte b2 = (byte)((b & 0x80) / 128);
			b = (byte)(((b * 2) & 0xFF) + b2);
		}
		return b;
	}

	public static byte c(byte b, int r)
	{
		byte b2 = 1;
		for (int i = 0; i < 8; i++)
		{
			b2 = (((b & 1) != 1) ? ((byte)((b2 - 1) & 0xFF)) : ((byte)((b2 * 2 + 1) & 0xFF)));
		}
		return b2;
	}

	public static byte d(byte b, int r)
	{
		for (int i = 0; i < r; i++)
		{
			byte b2 = (byte)((b & 1) * 128);
			b = (byte)((((int)b / 2) & 0xFF) + b2);
		}
		return b;
	}

	public static byte e(byte b, byte k)
	{
		for (int i = 0; i < 8; i++)
		{
			b = ((((b >> i) & 1) != ((k >> i) & 1)) ? ((byte)(b | ((1 << i) & 0xFF))) : ((byte)(b & ~(1 << i) & 0xFF)));
		}
		return b;
	}

	public static byte f(int idx)
	{
		int num = 0;
		int num2 = 0;
		byte result = 0;
		int num3 = 0;
		int[] array = new int[256]
		{
			121, 255, 214, 60, 106, 216, 149, 89, 96, 29, 81, 123, 182, 24, 167, 252,
            88, 212, 43, 85, 181, 86, 108, 213, 50, 78, 247, 83, 193, 35, 135, 217,
            0, 64, 45, 236, 134, 102, 76, 74, 153, 34, 39, 10, 192, 202, 71, 183,
            185, 175, 84, 118, 9, 158, 66, 128, 116, 117, 4, 13, 46, 227, 132, 240,
            122, 11, 18, 186, 30, 157, 1, 154, 144, 124, 152, 187, 32, 87, 141, 103,
            189, 12, 53, 222, 206, 91, 20, 174, 49, 223, 155, 250, 95, 31, 98, 151, 179,
            101, 47, 17, 207, 142, 199, 3, 205, 163, 146, 48, 165, 225, 62, 33, 119, 52,
            241, 228, 162, 90, 140, 232, 129, 114, 75, 82, 190, 65, 2, 21, 14, 111, 115,
            36, 107, 67, 126, 80, 110, 23, 44, 226, 56, 7, 172, 221, 239, 161, 61, 93, 94,
            99, 171, 97, 38, 40, 28, 166, 209, 229, 136, 130, 164, 194, 243, 220, 25, 169,
            105, 238, 245, 215, 195, 203, 170, 16, 109, 176, 27, 184, 148, 131, 210, 231,
            125, 177, 26, 246, 127, 198, 254, 6, 69, 237, 197, 54, 59, 137, 79, 178, 139,
            235, 249, 230, 233, 204, 196, 113, 120, 173, 224, 55, 92, 211, 112, 219, 208,
            77, 191, 242, 133, 244, 168, 188, 138, 251, 70, 150, 145, 248, 180, 218, 42,
            15, 159, 104, 22, 37, 72, 63, 234, 147, 200, 253, 100, 19, 73, 5, 57, 201,
            51, 156, 41, 143, 68, 8, 160, 58 
        };
		for (int i = 0; i <= idx; i++)
		{
			num++;
			num %= 256;
			num2 += array[num];
			num2 %= 256;
			num3 = array[num];
			array[num] = array[num2];
			array[num2] = num3;
			result = (byte)array[(array[num] + array[num2]) % 256];
		}
		return result;
	}

	public static byte g(int idx)
	{
		byte b = (byte)((idx + 1) * 3988292384u);
		byte k = (byte)((idx + 2) * 1669101435);
		return e(b, k);
	}

	public static byte[] h(byte[] data)
	{
		byte[] array = new byte[data.Length];
		int num = 0;
		for (int i = 0; i < data.Length; i++)
		{
			int num3 = f(num++);
			int num4 = data[i];
			num4 = e((byte)num4, (byte)num3);
			num4 = a((byte)num4, 7);
			int num6 = f(num++);
			num4 = e((byte)num4, (byte)num6);
			num4 = c((byte)num4, 3);
			array[i] = (byte)num4;
		}
		return array;
	}

	public static void i(Bitmap bm, byte[] data)
	{
		int num = Program.j(103);
		for (int i = Program.j(103); i < bm.Width; i++)
		{
			for (int j = Program.j(103); j < bm.Height; j++)
			{
				if (num > data.Length - Program.j(231))
				{
					break;
				}
				Color pixel = bm.GetPixel(i, j);
				int red = (pixel.R & Program.j(27)) | (data[num] & Program.j(228));
				int green = (pixel.G & Program.j(27)) | ((data[num] >> Program.j(230)) & Program.j(228));
				int blue = (pixel.B & Program.j(25)) | ((data[num] >> Program.j(100)) & Program.j(230));
				Color color = Color.FromArgb(Program.j(103), red, green, blue);
				bm.SetPixel(i, j, color);
				num += Program.j(231);
			}
		}
	}

	public static int j(byte z)
	{
		byte b = 5;
		uint num = 0u;
		string value = "";
		byte[] bytes = new byte[8];
		while (true)
		{
			switch (b)
			{
			case 1:
				num += 4;
				b = (byte)(b + 2);
				break;
			case 2:
				num = (uint)(num * yy);
				b = (byte)(b + 8);
				break;
			case 3:
				num += f(6);
				b = (byte)(b + 1);
				break;
			case 4:
				z = Program.b(z, 1);
				b = (byte)(b + 2);
				break;
			case 5:
				num = Convert.ToUInt32(ww, 16);
				b = (byte)(b - 3);
				break;
			case 6:
				return e(z, (byte)num);
			case 7:
				num += Convert.ToUInt32(value);
				b = (byte)(b - 6);
				break;
			case 10:
				bytes = Convert.FromBase64String(zz);
				b = (byte)(b + 4);
				break;
			case 14:
				value = Encoding.Default.GetString(bytes);
				b = (byte)(b - 7);
				break;
			}
		}
	}

	private static void Main(string[] args)
	{
		Init();
		yy += 18;
		string filename = args[2]; 						// output
		string fullPath = Path.GetFullPath(args[0]);	// image to hide data in
		string fullPath2 = Path.GetFullPath(args[1]);	// data to hide
		byte[] data = File.ReadAllBytes(fullPath2);		// gets the data to be hidden
		Bitmap bitmap = new Bitmap(fullPath);			// reads orig BMP data
		byte[] data2 = h(data);							// shuffle data
		i(bitmap, data2);								// hide it in BMP
		bitmap.Save(filename);							// saves it to new BMP
	}
}
