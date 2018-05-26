package notreepunching.recipe.knife;

import com.google.common.collect.LinkedListMultimap;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import notreepunching.NoTreePunching;
import notreepunching.item.ModItems;
import notreepunching.util.ItemUtil;

import java.util.ArrayList;
import java.util.List;

public class KnifeRecipeHandler {

    private static List<KnifeRecipe> CUTTING_RECIPES = new ArrayList<KnifeRecipe>();
    private static LinkedListMultimap<Boolean, KnifeRecipe> CT_ENTRY = LinkedListMultimap.create();

    public static void init(){
        for(int i=0;i<7;i++) {
            if(!NoTreePunching.replaceQuarkStones && (i == 4 || i == 5)) { continue; }
            if(!NoTreePunching.replaceRusticStone && (i == 6)) { continue; }
            addCuttingRecipe(new ItemStack(ModItems.rockStone, 1, i), new ItemStack(ModItems.flintShard, 1));
        }
        addCuttingRecipe(Items.FLINT, new ItemStack(ModItems.flintShard,2));

        addCuttingRecipe(Blocks.WOOL,new ItemStack(Items.STRING,4));
        addCuttingRecipe(Items.REEDS,new ItemStack(ModItems.grassFiber,2));
        addCuttingRecipe(Items.WHEAT,new ItemStack(ModItems.grassFiber,1), new ItemStack(Items.WHEAT_SEEDS));
        addCuttingRecipe(Blocks.SAPLING,new ItemStack(Items.STICK),new ItemStack(ModItems.grassFiber));

        addCuttingRecipe(Items.LEATHER_BOOTS,new ItemStack(Items.LEATHER,2));
        addCuttingRecipe(Items.LEATHER_CHESTPLATE,new ItemStack(Items.LEATHER,5));
        addCuttingRecipe(Items.LEATHER_LEGGINGS,new ItemStack(Items.LEATHER,4));
        addCuttingRecipe(Items.LEATHER_HELMET,new ItemStack(Items.LEATHER,3));

        addCuttingRecipe(Blocks.MELON_BLOCK,new ItemStack(Items.MELON,9));
        addCuttingRecipe(Items.MELON,new ItemStack(Items.MELON_SEEDS,1),new ItemStack(ModItems.grassFiber));
        addCuttingRecipe(Blocks.PUMPKIN,new ItemStack(Items.PUMPKIN_SEEDS,4),new ItemStack(ModItems.grassFiber,2));

        for(int i = 0; i<6; i++){
            addCuttingRecipe(new ItemStack(i<4 ? Blocks.LEAVES : Blocks.LEAVES2,6,i%4),new ItemStack(Blocks.SAPLING,1,i),new ItemStack(ModItems.grassFiber,2));
        }

    }

    public static void postInit(){
        CT_ENTRY.forEach((action, entry) -> {
            if(action){
                CUTTING_RECIPES.add(entry);
            }else {
                CUTTING_RECIPES.removeIf(p -> ItemUtil.areStacksEqual(entry.getInput(),p.getInput()));
            }
        });
    }

    public static boolean isRecipe(ItemStack stack){
        return getRecipe(stack)!=null;
    }

    public static KnifeRecipe getRecipe(ItemStack stack){
        for(int i=0;i<CUTTING_RECIPES.size();i++){
            ItemStack is = CUTTING_RECIPES.get(i).getInput();
            if(is.getItem() == stack.getItem() && stack.getCount()>=is.getCount() && is.getMetadata() == stack.getMetadata()){
                return CUTTING_RECIPES.get(i);
            }
        }
        return null;
    }

    public static List<KnifeRecipe> getAll(){
        return CUTTING_RECIPES;
    }

    private static void addCuttingRecipe(ItemStack inputStack, ItemStack... outputs){
        CUTTING_RECIPES.add(new KnifeRecipe(inputStack,outputs));
    }
    private static void addCuttingRecipe(Item input, ItemStack... outputs){
        addCuttingRecipe(new ItemStack(input,1,0),outputs);
    }
    private static void addCuttingRecipe(Block input, ItemStack... outputs){
        addCuttingRecipe(new ItemStack(input,1,0),outputs);
    }

    // Craft Tweaker
    public static void addEntry(KnifeRecipe recipe, boolean type){
        CT_ENTRY.put(type, recipe);
    }
}