# bookGather
Recreate book from HAR file

## Make Request example

### Create images from HAR file

**verb** = POST

*endpoint* = http://localhost:8080/generateFile

**params**
* **origin** = C:\Users\jaime\OneDrive\Escritorio\HAR files\まるごと　日本のことばと文化　入門　A１　りかい.json
* **destiny** = C:\Users\jaime\OneDrive\Escritorio\HAR files\images\
* **identification** = f4FIAgAAQBAJ

### Create PDF from images

**verb** = POST

*endpoint* = http://localhost:8080/createPDF

**params**
* **imagesPath** = C:\Users\jaime\OneDrive\Escritorio\HAR files\images\
* **elements** = 199
* **bookName** = marugoto