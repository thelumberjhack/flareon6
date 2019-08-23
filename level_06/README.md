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
```

## Resources

- [.NET Internals and Code Injection](https://www.codeproject.com/articles/26060/net-internals-and-code-injection)
