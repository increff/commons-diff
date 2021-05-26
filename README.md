# commons-diff
commons-diff library can be used to compare two directories or files

## Overview
The `increff-diff` library can be used to compare two directories in the following ways
1. Compare the Set of filenames in the two directories. This checks if the differ in count OR names (only logs a Warning if the Sets are different)
2. For each common file (files with common names) in the two directories, create a Set of lines contained in the two files and compare the same

For each common file, the result of comparison is displayed on the console. If there is a difference in the lines of the the two files, the same can be printed by calling the printDiff() method on the Set of differences in lines.

*Note*: Since the algorithm is based on comparing the Set of lines, repetitions of lines are ignored. This implies that if both files contain the same lines, repeated in different quantities (say 10 times in the first file and 5 times in the other), the files will be considered similar.

## Usage
###`main()`
`Diff.java` contains a `main()` method which can be run directly from a terminal. This requires two run-time arguments representing the directories to be compared.

`javac Diff.java directory_path_1 directory_path_2`

### pom.xml
Alternatively, to use increff-diff as a library, it must be included in pom.xml
```xml
<dependency>
    <groupId>com.increff.diff</groupId>
    <artifactId>commons-diff</artifactId>
    <version>${increff-diff.version}</version>
    <scope>test</scope>
</dependency>
```
## Key Classes
### Diff
The Diff class has a main() method which can be run directly, as stated above. Alternatively, the following methods may be used

- `compareDirectories(File dir_A, File dir_B)`: Compares the set of filenames in each directory and returns result as a log on the console. Constructs a set of all files whose filenames are common in both directories and then runs compareFiles() on each pair to compare files line-wise
- `compareFiles(File file_A, File file_B)`: Compares the inputted files line-wise. Creates a Set of lines for both the files and compares the two Sets.

##Example
Suppose the two files to be compared are listed as below
```
DirA\file.txt	
w, x, y, z
2, 3, 4, 5
0, 1, 2, 3

DirB\file.txt
w, x, y, z
0, 1, 2, 3
2, 2, 4, 5
```
To compare the two files, we call the compareFiles() function as

`Diff.compareFiles(new File("DirA\\file.txt"), new File("DirA\\file.txt"));`

The output logged on the console is as follows:
```
2020-07-11 12:31:39 INFO  Diff:80 - Comparing DirA\file.txt, DirB\file.txt
2020-07-11 12:31:39 WARN  Diff:89 - Files are different
2020-07-11 12:31:39 WARN  Diff:97 - <<< 2, 3, 4, 5
2020-07-11 12:31:39 WARN  Diff:97 - >>> 2, 2, 4, 5
```
## License
Copyright (c) Increff

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License
is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
or implied. See the License for the specific language governing permissions and limitations under
the License.
