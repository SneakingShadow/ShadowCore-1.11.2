# Shadow-Core
My own little core mod.

Most notably this mod has utilities for checking for multiblocks in the world.


## MultiBlock:

A MultiBlock object will use its specified parameters as a basis for building a three dimensional array, representing the structure in world.  
Parameters can be of any object, but will either be converted or disregarded if invalid.

InputList can be used to use the same parameters multiple times.  
The class extends ArrayList\<Object\>, and is treated differently than normal ArrayLists.  
Everything in the InputList will be copied to the MultiBlock's parameter list, as if manually inputted by hand.  

null will be converted to an always valid StructureBlock.  
Items will be converted to Blocks if possible, or null.  
Blocks will be converted to a StructureBlock checking for that block disregarding metadata.  
ItemStacks will be converted to a StructureBlock that checks for the Item converted to a Block, with the same metadata as the ItemStack's damage, if applicable.  
ArrayLists will be converted to a StructureBlock that's valid when one of the object stored are valid.  
Special characters will be converted to their specified StructureBlock.  
Operators, after having taken its operands, will also be converted into a StructureBlock.  

To make debugging simpler, the toString function was overridden and returns the array as a string.  
Note: The array is givven in order of the smallest to largest axes.  


## public non-static functions:

setRotationXAxis(boolean bool)  default: false  
setRotationYAxis(boolean bool)  default: true  
setRotationZAxis(boolean bool)  default: false  
  * Sets rotation around axis.  
  * Rotation around axis is against/with the clock, when looking at the structure dead on from specified axis.  

rotatesAroundXAxis()  
rotatesAroundYAxis()  
rotatesAroundZAxis()  
  * Returns boolean  

findStructure(World world, int x, int y, int z)  
  * Will try to find one structure overlapping specified coordinate in the world, and stops when it has found one.  
  * Returns a new Structure or null if none were found.  

findStructures(World world, int x, int y, int z)  
  * Will try to find all structures overlapping specified coordinate the world.  
  * Returns an ArrayList\<Stucture\> that's empty if none are found.  

validate(World world, Vec3 cornerPosition, int rotationX, int rotationY, int rotationZ)  
  * Used by Structure to validate if it's still valid.  

sizeX()  
sizeY()  
sizeZ()  
  * Returns the size of the three dimensional array.  

toString()  
  * Returns the array in string format, with the axis with the lowest size first.  

debugStructureArray()  
  * Prints the structure array in the console.  


## Parameters:  

### Special Characters:  
  * \' \' = anything. doesn\'t matter what block it is.  
    * true  
  * \'\+\' = full block  
    * block.isOpaqueCube()  
  * \'\_\' = air block  
    * block.isAir(world, x,y,z)  
  * \'\-\' = replaceable block  
    * block.isReplaceable(world,x,y,z)  
  * \'~\' = liquid  
    * block.getMaterial().isLiquid()  
  * \'\*\' = opaque material  
    * block.getMaterial().isOpaque()  
  * \'\#\' = opaque light based  
    * block.getLightOpacity(world, x,y,z) == 255  
    
---

### Modifier:  
  * \'@\' = OreDictionary  
    * If inputted as a character, next string will be assumed to be an ore-name.    
    * If used in string, the ore-name has to be encased in @  
   
---

### Mapping:  
  * \'^\' = map object to next string  
    * If inputted as a character, the next string will be used as key to map the object after that.  
    * The mapped object can be used by encasing the key in ^. 
    * Example:  
      * ''' new MultiBlock( \'^\', "cobble", Blocks.cobblestone, "^cobble^^cobble^^cobble^" ) '''  
      * A line of three cobblestone.  
  * character = map object to character  
    * If a non special character is inputted, the next object will be mapped to that character.  
    * The mapped object can be used by using the key in a string, that does not serve another purpose.  
    * Example:  
      * ''' new MultiBlock( "ccc", \'c\', Blocks.cobblestone ) '''  
      * A line of three cobblestone.  

---
    
### Structure Modifier:  
  * \'/\' = next z column.  
    * z++ ; x=0  
  * \'\\\' = next level up.  
    * y++ ; x=0 ; z=0  

---

### Duplicators:  
  * \'\<\' = level 0,  
  * \'\>\' = level 1,  
  * \'\*\' = level 2,  
  *          
  * Duplicators take an operand before it, and an integer operand after it.  
  * The first operand is duplicated into the amount specified by the second operand.  
  * Note:  
    * This operand will be ignored if no integer is found, or integer is lower than 1.  

---

### Operators, in order of precedence:  
  * \'(\' and \')\' = Brackets  
    * Can be used inside of a string, and outside as characters.  
    * Everything between two brackets will be put into an ArrayList.  
  * \'\!\' = not       
    * takes one operand after it.  
    * Inverts the next check  
  * \'&\' = and       
    * takes two operands, one before and one after.  
    * Both cases have to be true  
  * \'|\' = or        
    * takes two operands, one before and one after.  
    * One of the cases have to be true.  
    * If operator is duplicated or mapped to a key, everywhere that same operator is used, it has to give have the same case value.  
    * Example:  
      * ''' new MultiBlock("xxx", \'x\', Blocks.cobblestone, \'|\', Blocks.sand) '''  
      * A line of three cobble, or three sand. Can't be intermixed.  

---

### Order of precedence:  
  * Duplicator, level 0  
  * Extraction of InputLists  
  * Brackets  
  * Convert and go through ArrayLists in this order of precedence, just without Structure Modifiers  
  * Modifiers  
  * Special values  
  * Duplicator, level 1  
  * Operators  
  * Duplicator, level 2  
  * Mapping  
  * Structure Modifiers  

