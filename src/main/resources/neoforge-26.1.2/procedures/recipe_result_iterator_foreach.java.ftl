for (ItemStack itemstack : ${input$result}) {
    ItemStack itemstackiterator = itemstack.copy();
    ${statement$foreach}
}