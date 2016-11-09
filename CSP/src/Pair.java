/**
 * Created by benjihannam on 10/31/16.
 */
public class Pair {
    public int one;
    public int two;

    public Pair(int first, int second){

        one = first;
        two = second;
    }

    @Override
    public int hashCode() {
        return 10*one + 5*two;
    }

    public boolean contains(int number){
        return one == number || two == number;
    }

    public boolean equals(Object other){
        return one == ((Pair) other).one && two == ((Pair)other).two;
    }

    public String toString(){
        return "(" + one + ", " + two + ")";
    }
}
