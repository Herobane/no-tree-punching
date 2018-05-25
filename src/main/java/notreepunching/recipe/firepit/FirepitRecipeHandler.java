package notreepunching.recipe.firepit;

import com.google.common.collect.LinkedListMultimap;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;
import notreepunching.util.ItemUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirepitRecipeHandler {

    private static List<FirepitRecipe> FIREPIT_RECIPES = new ArrayList<FirepitRecipe>();
    private static LinkedListMultimap<Boolean, FirepitRecipe> CT_ENTRY = LinkedListMultimap.create();

    public static void postInit(){
        Map<ItemStack, ItemStack> map = FurnaceRecipes.instance().getSmeltingList();

        for(Map.Entry<ItemStack, ItemStack> m : map.entrySet()){
            if(m.getValue().getItem() instanceof ItemFood){
                int meta1 = m.getKey().getMetadata() == OreDictionary.WILDCARD_VALUE ? 0 : m.getKey().getMetadata();
                int meta2 = m.getValue().getMetadata() == OreDictionary.WILDCARD_VALUE ? 0 : m.getValue().getMetadata();

                addFirepitRecipe(new ItemStack(m.getKey().getItem(),1,meta1),new ItemStack(m.getValue().getItem(),1,meta2));
            }
        }

        CT_ENTRY.forEach((action, entry) -> {
            if(action){
                FIREPIT_RECIPES.add(entry);
            }else {
                FIREPIT_RECIPES.removeIf(p -> ItemUtil.areStacksEqual(entry.getOutput(),p.getOutput()));
            }
        });
    }

    public static boolean isRecipe(ItemStack stack) { return getRecipe(stack) != null; }

    public static FirepitRecipe getRecipe(ItemStack stack){
        if(stack.isEmpty()) return null;

        for(FirepitRecipe recipe : FIREPIT_RECIPES){
            ItemStack is = recipe.getInput();
            if(is.getItem().getUnlocalizedName().equals(stack.getItem().getUnlocalizedName()) && is.getMetadata() == stack.getMetadata() && stack.getCount()>=is.getCount()){
                return recipe;
            }
        }
        return null;
    }

    public static List<FirepitRecipe> getAll(){
        return FIREPIT_RECIPES;
    }

    private static void addFirepitRecipe(ItemStack input, ItemStack output){
        FIREPIT_RECIPES.add(new FirepitRecipe(input,output));
    }

    // Craft Tweaker
    public static void addEntry(FirepitRecipe recipe, boolean type){
        CT_ENTRY.put(type, recipe);
    }

}
