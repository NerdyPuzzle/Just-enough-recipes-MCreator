for (ItemStack _itemstack : ${input$result}) {
    ItemStack itemstackiterator = _itemstack.copy();
    ${statement$foreach}
}