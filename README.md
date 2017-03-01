# Graph
graph with memory manager and handle


Author: Shuaicheng Zhang
Invocation and I/O Files:
The program would be invoked from the command-line as:
java GraphProject {memFile} {numBuffs} {buffSize} {initHashSize} {commandFile} {statFile}
The name of the program is GraphProject. Parameter {memFile} is the name of the ﬁle that will store the memory pool. Parameter {numBuffs} determines the number of buﬀers allocated for the buﬀer pool. Parameter {buffSize} is the size for a block in the memory pool (and so also the size of a buﬀer in the buﬀer pool). Parameter {initHashSize} is the initial size of the hash table (in terms of slots). The program reads from text ﬁle {commandFile} a series of commands, with one command per line.

print {artist|song|blocks|graph} This is identical to Project 1, except for the addition of the graph option. When the graph
option is given you will do the following:
1. Compute connected components on the graph. (the Union/FIND algorithm) Print out the number of connected components, and the size of the largest connected component.
2. Compute (and print) the diameter for the largest connected component using Floyd’s algo-rithm for computing all-pairs shortest paths.
