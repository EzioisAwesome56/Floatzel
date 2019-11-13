package com.eziosoft.floatzel.Util;

public class Brainfuck {

    // Eziosoft Brainfuck interpreter
    public static String ParseBrainFuck(String fuck){
        // get length of program
        int len = fuck.length();
        // init brainfuck enviroment
        int count = 0;
        int cell = 0;
        int[] mem = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        String[] charset = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p",
        "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        String out = "";
        String instruct = "";
        String temp = "";
        // actually run it
        while (count != len){
            instruct = fuck.substring(count, count + 1);
            if (instruct.equals(">")){
                cell++;
            } else if (instruct.equals("<")){
                cell--;
            } else if (instruct.equals("+")){
                mem[cell]++;
            } else if (instruct.equals("-")){
                mem[cell]--;
            } else if (instruct.equals(".")){
                out = out + charset[mem[cell]];
            } else if (instruct.equals(",")){
                // deviate slightly from normal Brainfuck
                // go to next character and load that
                count++;
                mem[cell] = Integer.valueOf(fuck.substring(count, count + 1));
            } else if (instruct.equals("[")){
                // is the current cell zero?
                if (mem[cell] == 0){
                    // find the ]
                    while (!fuck.substring(count, count + 1).equals("]")){
                        count++;
                    }
                }
            } else if (instruct.equals("]")){
                // is the current cell not zero?
                if (mem[cell] != 0){
                    // find the [!
                    while (!fuck.substring(count, count + 1).equals("[")){
                        count--;
                    }
                }
            }
            // inc the counter by 1
            count++;

        }
        return out;
    }
}
