// Run with:  javac *.java   then   java ListArrayBasedTest
public class ListArrayBasedTest
{
  private static int passed = 0;
  private static int failed = 0;

  public static void main(String[] args)
  {
    ListArrayBased list = new ListArrayBased();

    System.out.println("=== 1. New list: isEmpty() and size() ===");
    displayList(list);
    check("new list is empty", list.isEmpty());
    check("new list size is 0", list.size() == 0);

    System.out.println("\n=== 2. add() ===");
    list.add(1, "A");                 // [A]
    displayList(list);
    list.add(2, "C");                 // [A, C]   
    displayList(list);
    list.add(2, "B");                 // [A, B, C] 
    displayList(list);
    list.add(1, "Z");                 // [Z, A, B, C] 
    displayList(list);
    check("list is no longer empty", !list.isEmpty());
    check("size is 4 after four adds", list.size() == 4);
    check("item 1 is Z", "Z".equals(list.get(1)));
    check("item 2 is A", "A".equals(list.get(2)));
    check("item 3 is B", "B".equals(list.get(3)));
    check("item 4 is C", "C".equals(list.get(4)));

    System.out.println("\n=== 3. get() ===");
    check("get(1) returns first item", "Z".equals(list.get(1)));
    check("get(size()) returns last item", "C".equals(list.get(list.size())));
    check("get() does not change size", list.size() == 4);

    System.out.println("\n=== 4. remove() ===");
    list.remove(1);                   // remove first  -> [A, B, C]
    displayList(list);
    check("size is 3 after removing first", list.size() == 3);
    check("item 1 is now A", "A".equals(list.get(1)));

    list.remove(2);                   // remove middle -> [A, C]
    displayList(list);
    check("size is 2 after removing middle", list.size() == 2);
    check("item 2 is now C", "C".equals(list.get(2)));

    list.remove(2);                   // remove last   -> [A]
    displayList(list);
    check("size is 1 after removing last", list.size() == 1);
    check("item 1 is still A", "A".equals(list.get(1)));

    list.remove(1);                   // remove only item -> []
    displayList(list);
    check("list is empty after removing every item", list.isEmpty());

    System.out.println("\n=== 5. removeAll() ===");
    for (int i = 1; i <= 5; i++)
    {
      list.add(i, "item" + i);
    }
    displayList(list);
    check("size is 5 before removeAll", list.size() == 5);
    list.removeAll();
    displayList(list);
    check("list is empty after removeAll", list.isEmpty());
    check("size is 0 after removeAll", list.size() == 0);
    list.add(1, "again");             // list still usable afterwards
    check("list usable after removeAll", "again".equals(list.get(1)));
    list.removeAll();

    System.out.println("\n=== 6. Storing different object types ===");
    list.add(1, Integer.valueOf(42));
    list.add(2, "text");
    list.add(3, Double.valueOf(3.14));
    list.add(4, null);
    displayList(list);
    check("Integer stored and retrieved", Integer.valueOf(42).equals(list.get(1)));
    check("null can be stored", list.get(4) == null);
    list.removeAll();

    System.out.println("\n=== 7. Exceptions: index out of range ===");
    list.add(1, "X");
    list.add(2, "Y");                 // [X, Y], size 2
    displayList(list);

    expectIndexException("add(0, ...)",            () -> list.add(0, "bad"));
    expectIndexException("add(size()+2, ...)",     () -> list.add(list.size() + 2, "bad"));
    expectIndexException("add(-1, ...)",           () -> list.add(-1, "bad"));
    expectIndexException("get(0)",                 () -> list.get(0));
    expectIndexException("get(size()+1)",          () -> list.get(list.size() + 1));
    expectIndexException("remove(0)",              () -> list.remove(0));
    expectIndexException("remove(size()+1)",       () -> list.remove(list.size() + 1));
    check("size unchanged after failed operations", list.size() == 2);

    ListArrayBased empty = new ListArrayBased();
    expectIndexException("get(1) on empty list",    () -> empty.get(1));
    expectIndexException("remove(1) on empty list", () -> empty.remove(1));
    list.removeAll();

    System.out.println("\n=== 8. Filling the list to capacity (MAX_LIST = 50) ===");
    for (int i = 1; i <= 50; i++)
    {
      list.add(i, "n" + i);
    }
    check("list holds 50 items", list.size() == 50);
    check("first item is n1", "n1".equals(list.get(1)));
    check("last item is n50", "n50".equals(list.get(50)));

    // The list is now full. 51st should be rejected
    try
    {
      list.add(51, "overflow");
      check("adding to a FULL list throws an exception", false);
    }
    catch (ListException e)
    {
      check("adding to a FULL list throws ListException", true);
    }
    catch (RuntimeException e)
    {
      check("adding to a FULL list throws ListException (got "
            + e.getClass().getSimpleName() + " instead)", false);
    }
    check("size still 50 after rejected add", list.size() == 50);

    System.out.println("\n=== Summary ===");
    System.out.println("Passed: " + passed);
    System.out.println("Failed: " + failed);
  } // end main

  
  public static void displayList(ListArrayBased list)
  {
    StringBuilder sb = new StringBuilder();
    sb.append("List (size ").append(list.size()).append("): [");
    for (int i = 1; i <= list.size(); i++)
    {
      sb.append(list.get(i));
      if (i < list.size())
      {
        sb.append(", ");
      }
    }
    sb.append("]");
    if (list.isEmpty())
    {
      sb.append("  (empty)");
    }
    System.out.println(sb);
  } // end displayList

  
  private static void check(String description, boolean condition)
  {
    if (condition)
    {
      passed++;
      System.out.println("  PASS: " + description);
    }
    else
    {
      failed++;
      System.out.println("  FAIL: " + description);
    }
  } // end check

  private static void expectIndexException(String description, Runnable action)
  {
    try
    {
      action.run();
      check(description + " throws ListIndexOutOfBoundsException", false);
    }
    catch (ListIndexOutOfBoundsException e)
    {
      check(description + " throws ListIndexOutOfBoundsException", true);
    }
    catch (RuntimeException e)
    {
      check(description + " throws ListIndexOutOfBoundsException (got "
            + e.getClass().getSimpleName() + ")", false);
    }
  } // end expectIndexException
} // end ListArrayBasedTest
