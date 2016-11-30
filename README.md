# SneakingShadow-s-Core-Mod
My own little core mod.

Most notably it has/adds utilities for checking for multiblocks in the world.

The multiblock functionality is used by creating a MultiBlock object, 
which is called upon in order to check for that multiblock in the world. 

The parameters inputted upon initialization will decide the structure of the multiblock, and will be stored in an array. 

Blocks in the world will be compared to the objects in the array, that all extends the StructureBlock class. 

The parameters that don't extend StructureBlock will be converted into structure blocks, or disregarded without throwing an error.

To make debugging simpler, the toString function was overridden and returns the array as a string. 
Note: The array is givven in order of the smallest to largest axes.



Special Characters / Values:
    ' ' or null = anything. doesn't matter what block it is.
        true
    '+' = full block
        block.isOpaqueCube()
    '_' = air block
        block.isAir(world, x,y,z)
    '-' = replaceable block
        block.isReplaceable(world,x,y,z)
    '~' = liquid
        block.getMaterial().isLiquid()
    '*' = opaque material
        block.getMaterial().isOpaque()
    '#' = opaque light based
        block.getLightOpacity(world, x,y,z) == 255
    
Modifier:
    '@' = OreDictionary
        If inputted as a character, next string will be assumed to be an ore-name.
        If used in string, the ore-name has to be encased in @
            
Mapping:
    '^' = map object to next string
        If inputted as a character, the next string will be used as key to map the object after that.
        The mapped object can be used by encasing the key in ^. 
        Example:
            new MultiBlock( '^', "cobble", Blocks.cobblestone, "^cobble^^cobble^^cobble^" )
            A line of three cobblestone.
    character = map object to character
        If a non special character is inputted, the next object will be mapped to that character.
        The mapped object can be used by using the key in a string, that does not serve another purpose. 
        Example:
            new MultiBlock( "ccc", 'c', Blocks.cobblestone )
            A line of three cobblestone.
    
Structure Modifier:
    '/' = next z column.
        z++  x=0
    '\' = next level up.
        y++  x=0  z=0

Duplicators:
    '<' = level 0
    '>' = level 1
    '-' = level 2

    Duplicators take an operand before it, and an integer operand after it.
    It duplicates the value before it into the amount that the integer specifies
    Note:
        This operand will be ignored if no integer is found, or integer is lower than 1.

Operators, in order of precedence:
    '(' and ')' = Brackets
        Can be used inside of a string, and outside as characters. 
        Everything between two brackets will be put into an ArrayList.
    '!' = not       takes one operand
        Inverts the next check
    '&' = and       takes two operands
        Both cases have to be true
    '|' = or        takes two operands
        One of the cases have to be true.
        If it's mapped to something, everything it's mapped to has to be the same.
        A, '|', B to character 'l', then everywhere l is used in place of (A, '|', B) has to yield the same result;
        meaning, if you get A,A,A,B it's invalid, but if it's only A or only B, then it's valid.

Order of precedence:
    Duplicator, level 0
    Extraction of InputLists
    Brackets
    Sort any found ArrayLists in this order of precedence, just without Structure Modifiers
    Modifiers
    Special values
    Duplicator, level 1
    Operators
    Duplicator, level 2
    Mapping
    Structure Modifiers
