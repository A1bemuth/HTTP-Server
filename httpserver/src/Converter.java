import com.sun.net.httpserver.*;

interface Conv {
    public String toFinalBase();
}

public class Converter implements Conv {
    private String input;
    private int baseCurr, baseFinal;
    public Converter(int[] mass){
        input = Integer.toString(mass[0]);
        baseCurr = mass[1];
        baseFinal = mass[2];
    }
    public Converter(String num,int base1,int base2){
        input = num;
        baseCurr = base1;
        baseFinal = base2;
    }

    //Создаёт объект Converter из принятой от сервера строки st,
    //элементы которой разделены символами separator.
    public Converter(String st, String separator){
        String[] parse = st.split(separator);
        input = parse[0];
        baseCurr = Integer.parseInt(parse[1]);
        baseFinal = Integer.parseInt(parse[2]);
    }
    //Возвращает строку, представляющую число input
    //в системе счисления baseFinal.
    public String toFinalBase(){
        String ans;
        int decBase = Integer.parseInt(input, baseCurr); //Перевод input в десятичную систему счисления
        ans = Integer.toString(decBase, baseFinal); //Перевод исходного числа в систему счисления baseFinal
        return ans;
    }

}