import os

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

    lang.writeComment("Tongs")
    for [metal, name] in TFC_METALS.items():
        lang.writeItem(f"hotornot.metal.tongs.{metal}", f"{name} Tongs")
        lang.writeItem(f"hotornot.metal.tongs_head.{metal}", f"{name} Tongs Head")


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

    for metal in TFC_METALS:
        models.createItem(f"metal/tongs/{metal}", f"hotornot:items/metal/tongs/{metal}")
        models.createItem(f"metal/tongs_head/{metal}", f"hotornot:items/metal/tongs_head/{metal}")
        # Metal tongs recipe
        recipes.createRecipeAdvanced(f"metal/tongs/{metal}", "tfc:shaped_skill",
                                     ["I I", " S ", "S S"],
                                     {
                                         "I": Ingredient(itemID=f"hotornot:metal/tongs_head/{metal}"),
                                         "S": Ingredient(ore="stickWood")},
                                     Result(f"hotornot:metal/tongs/{metal}"))


if __name__ == "__main__":
    main()
    print("Resources Generated!")
