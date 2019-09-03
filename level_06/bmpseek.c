#include <stdlib.h>
#include <stdio.h>


char *read_bmp(FILE *bmpfile) {

    return 0;
}


int main(int argc, char const *argv[]) {
    const char *bmppath = "./bmphide/image.bmp";
    FILE *bmp = fopen(bmppath, "rb");
    size_t file_size = 0;

    if (bmp == NULL) {
        printf("Could not read file.\n");
        return EXIT_FAILURE;
    }

    // Geting file size
    fseek(bmp, 0L, SEEK_END);
    file_size = ftell(bmp);

    // Back to beginning of file
    fseek(bmp, 0L, SEEK_SET);

    char *data = (char *) malloc(file_size * sizeof(char));

    if (fread(&data, sizeof(char), file_size, bmp) != file_size){
        printf("Could not read the file.\n");
        fclose(bmp);
        return EXIT_FAILURE;
    } 

    if (fclose(bmp) == EOF) {
        printf("There was an issue closing the file.\n");
        return EXIT_FAILURE;
    }

    return EXIT_SUCCESS;
}

