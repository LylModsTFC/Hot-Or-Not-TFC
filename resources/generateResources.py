import os

import MinecraftDataGen.Util
from MinecraftDataGen import Models as models
from MinecraftDataGen import Recipes as recipes
from MinecraftDataGen.Lang import Lang
from MinecraftDataGen.Recipes import Ingredient, Result

TFC_METALS = {
    "copper": "Copper",
    "bronze": "Bronze",
    "bismuth_bronze": "Bismuth Bronze",
    "black_bronze": "Black Bronze",
    "wrought_iron": "Wrought Iron",
    "steel": "Steel",
    "black_steel": "Black Steel",
    "blue_steel": "Blue Steel",
    "red_steel": "Red Steel"
}

MOLD_METALS = [
    "copper",
    "bronze",
    "bismuth_bronze",
    "black_bronze"
]


def generateLang() -> None:
    lang = Lang("en_us")

    lang.writeHeader("Tooltips")
    lang.write("tooltip.hotornot.toohot", "Too hot to handle! Wear protection.")
    lang.write("tooltip.hotornot.toocold", "Too cold to handle! Wear protection.")
    lang.write("tooltip.hotornot.toolight", "Too light to handle!")
    lang.write("tooltip.hotornot.hot_holder_tooltip", "Hold in the offhand to avoid negative effects")
    lang.newLine()

    lang.writeHeader("Creative Tab")
    lang.write("itemGroup.hotornot", "Hot or Not - TFC")
    lang.newLine()

    lang.writeHeader("Items")
    lang.writeItem("hotornot.wooden_tongs", "Wooden Tongs")
    lang.writeItem("hotornot.mitts", "Mitts")
    lang.newLine()
    lang.writeComment("Molds")
    lang.writeItem("hotornot.ceramics.unfired.mold.tongs_jaw", "Unfired Tongs Jaw Mold")
    lang.writeItem("hotornot.ceramics.fired.mold.tongs_jaw", "Fired Tongs Jaw Mold")
    for moldMetal in MOLD_METALS:
        lang.writeItem(f"hotornot.ceramics.fired.mold.tongs_jaw.{moldMetal}", f"{TFC_METALS[moldMetal]} Tongs Jaw Mold")
    lang.newLine()

    lang.writeComment("Tongs")
    for [metal, name] in TFC_METALS.items():
        lang.writeItem(f"hotornot.metal.tongs.{metal}", f"{name} Tongs")
        lang.writeItem(f"hotornot.metal.tongs_jaw.{metal}", f"{name} Tongs Jaw Piece")


def main() -> None:
    os.chdir("../src/main/resources/assets/hotornot/")
    generateLang()

    models.createItem("mitts", "hotornot:items/mitts")
    recipes.createShaped("mitts", [" C ", "CLC", " CS"],
                         {"C": Ingredient(ore="clothHighQuality"),
                          "L": Ingredient(ore="leather"),
                          "S": Ingredient(ore="string")},
                         Result("hotornot:mitts"))

    models.createItem("wooden_tongs", "hotornot:items/wooden_tongs")
    recipes.createShaped("wooden_tongs", ["S S", " S ", "S S"], {"S": Ingredient(ore="stickWood")},
                         Result("hotornot:wooden_tongs"))

    models.createItem("ceramics/unfired/mold/tongs_jaw", "hotornot:items/ceramics/unfired/mold/tongs_jaw")
    models.createItem("ceramics/fired/mold/tongs_jaw/empty", "hotornot:items/ceramics/fired/mold/tongs_jaw/empty")

    for moldMetal in MOLD_METALS:
        models.createItem(f"ceramics/fired/mold/tongs_jaw/{moldMetal}",
                          f"hotornot:items/ceramics/fired/mold/tongs_jaw/{moldMetal}")

    for metal in TFC_METALS:
        models.createItem(f"metal/tongs/{metal}", f"hotornot:items/metal/tongs/{metal}")
        models.createItem(f"metal/tongs_jaw/{metal}", f"hotornot:items/metal/tongs_jaw/{metal}")
        # Metal tongs recipe
        recipes.createRecipeAdvanced(f"metal/tongs/{metal}", "tfc:shaped_skill",
                                     ["I I", " S ", "S S"],
                                     {
                                         "I": Ingredient(itemID=f"hotornot:metal/tongs_jaw/{metal}"),
                                         "S": Ingredient(ore="stickWood")},
                                     Result(f"hotornot:metal/tongs/{metal}"))

    # Recipe factories, needed for our mold recipe. Generated so we can delete all the recipes comfortably
    MinecraftDataGen.Util.writeDictToJson("recipes/_factories", {
        "recipes": {
            "unmold_recipe": "com.buuz135.hotornot.object.recipe.UnMoldJawPiece$Factory"
        }
    })

    # Tongs mold recipe, might as well generate it I douno
    MinecraftDataGen.Util.writeDictToJson("recipes/metal/unmold/tongs_jaw", {
        "type": "hotornot:unmold_recipe",
        "ingredients": [
            {
                "item": "hotornot:ceramics/fired/mold/tongs_jaw"
            }
        ]
    })


if __name__ == "__main__":
    main()
    print("Resources Generated!")
