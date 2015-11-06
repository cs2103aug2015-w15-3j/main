# A0124745Eunused
###### src\main\raijin\common\datatypes\Constants.java
``` java
  // Currently using images instead of string feedback messages.
  public static final String HELP_MESSAGE = "\n<----==== Raijin to the Rescue! ====---->"
      + "\nADD <Task Name>\nAdds a task with a specified name.\n"
      + "ADD <Task Name> <DateTime*>\nAdds a task with specified name and timeline.\n"
      + "ADD <Task Name> <DateTime*> <Tag*> <Priority*>"
      + "\nAdds a task with specified name, timeline, tag and priority.\n"
      + "\nEDIT <Task ID> <Task Name>\nEdits a task's name based on its associated ID.\n"
      + "EDIT <Task ID> <DateTime*>\nEdits a task's timeline* based on its associated ID.\n"
      + "EDIT <Task ID> <Task Name> <DateTime*>\nEdits both the task's name and timeline.\n"
      + "EDIT <Task ID> <Task Name> <DateTime*> <Tag*> <Priority*>"
      + "\nEdits the task's name, timeline, tag and priority.\n"
      + "\nDELETE <Task ID> or <Tag*>\nDelete a task.\n"
      + "\nDISPLAY <DateTime*> or <Type>"
      + "\nDisplay list of tasks depending on type or timeline specified.\n"
      + "Types: \"c\" for completed tasks, \"o\" for overdue tasks,"
      + " \"f\" for floating tasks and \"a\" for all tasks.\n" 
      + "Default: Pending tasks.\n"
      + "\nDONE <Task ID> or <Tag*>\nMark a task as done.\n"
      + "\nSEARCH <Keywords> <Tag*> <Priority*>"
      + "\nSearches for tasks based on keywords, tags or priorities.\n"
      + "\nSET <File path>\nSets a directory as the destination for the storage file.\n"
      + "\nUNDO/REDO\nUndo a command you previously did (CTRL+Z),"
      + " or redo a command you undid (CTRL+Y).\n"
      + "\n*DateTime: Can be input in flexible formats. Below are relevant examples:\n"
      + "by 18/3\ton 18/3 1800\tfrom 18/3 1800 to 2000\tfrom 18/3 1800 till 19/3 2000\n"
      + "*Tag: Tags need to include a \"#\" in front of them. Examples: #work #school\n"
      + "*Priority: Priorities have 3 options: !l, !m, and !h, which means !low,"
      + " !medium and !high respectively.\n"
      + "\n***** KEYBOARD SHORTCUTS *****\nCTRL+SPACE: Hide/Unhiding."
      + "\nF1/F2: Toggle between display views."
      + "\nTAB: Autocomplete names, or changes names to ID for EDIT/DELETE/DONE."
      + "\nUP/DOWN: Cycles through previously executed commands.\n";

  //============
  // Add command
  //============
```
