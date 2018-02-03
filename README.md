# imgds
2 image differ scorer.

* Input:2 image file paths
* Output: Degree of similarity

# Usage
```java
Scorer socorer = new Scorer("index");
String TEST_IMAGE_DIR = "src/test/resources/images";

// 1_1 and 1_2 are alike. They and 2_1 are not alike.
Path EXIST_IMG_1_1 = FileSystems.getDefault().getPath(TEST_IMAGE_DIR, "1_1.jpg");
Path EXIST_IMG_1_2 = FileSystems.getDefault().getPath(TEST_IMAGE_DIR, "1_2.jpg");
Path EXIST_IMG_2_1= FileSystems.getDefault().getPath(TEST_IMAGE_DIR, "2_1.jpg");
    
double hiScore = socorer.score(EXIST_IMG_1_1, EXIST_IMG_1_2);
double lowScore = socorer.score(EXIST_IMG_1_1, EXIST_IMG_2_1);
```
