List<Integer> _ints${cbi} = _shapeless ? _orderedCounts : recipe.integers();
for (int _num : _ints${cbi}) {
    double numberiterator = (double) _num;
    ${statement$foreach}
}