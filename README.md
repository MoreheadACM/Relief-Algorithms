Relief Algorithm Implementations
================================

Just dug out an old project and decided to rewrite it for code samples.

-Aaron


How Does It Work?
=================

It'll read the "data" file, which is basically a csv, with N samples on their own lines. Each sample has M features, which are separated by commas, and the last int on each line is the sample's class.

```
1,2,3
4,5,6
7,8,9
```

For example, if the "data" file contained what is above, that would mean you have 3 samples, with 2 features each. The first sample's first feature is 1, it's second is 2, and it's class is 3. Then the same idea for the other 2 samples.