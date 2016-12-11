package pl.edu.agh.polynomial.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import static pl.edu.agh.polynomial.Polynomial.skin;


/**
 * Created by Piotr Muras on 10.12.2016.
 */

public class MiejscaZerowe extends State {
    private Complex poly[];
    private Image bg;

    private class Complex{
        private double real;
        private double imaginary;

        public Complex(double real, double imaginary) {
            this.imaginary = imaginary;
            this.real = real;
        }

        public Complex(double real) {
            this.real = real;
            imaginary=0;
        }

        public Complex(Complex a){
            real=a.getReal();
            imaginary=a.getImaginary();
        }

        public double getReal() {
            return real;
        }

        public double getImaginary() {
            return imaginary;
        }

        public Complex multiplication(Complex b){
            Complex a = new Complex(real*b.getReal()-imaginary*b.getImaginary(),real*b.getImaginary()+imaginary*b.getReal());
            return a;
        }

        public  Complex multiplication(double b){
            Complex a = new Complex(real*b , imaginary*b);
            return a;
        }
        public Complex power(int b){
            if(b==0){
                return new Complex(1);
            }
            Complex a = new Complex(this);
            for(int i=0 ; i<b-1 ; i++){
               a= a.multiplication(this);
            }
            return a;
        }
        public Complex plus(Complex b){
            Complex a = new Complex(real+b.getReal() , imaginary+b.getImaginary());
            return a;
        }
        public Complex minus(Complex b){
            Complex a = new Complex(real-b.getReal() , imaginary-b.getImaginary());
            return a;
        }
        public double abs(){
            return Math.sqrt(real*real+imaginary*imaginary);
        }
        public double arg(){
            double argument;
            if(real>0){
                argument =Math.atan(imaginary/real);
            }
            else{
                if(real<0){
                    argument = Math.atan(imaginary/real) + Math.PI;
                }
                else {
                    if(imaginary>0){
                        argument=Math.PI/2;
                    }
                    else {
                        argument = -Math.PI/2;
                    }
                }
            }
            if(argument<0){
                argument+=2*Math.PI;
            }
            return argument;
        }
        public Complex sqrt(){
            if(real==0 && imaginary==0){
                return new Complex(0);
            }
            return new Complex(abs()/2*Math.cos(arg()/2) ,abs()/2*Math.sin(arg()/2) );
        }
        public Complex sprzezenie(){
            return new Complex(real , -imaginary);
        }
        public Complex divide(Complex b){
            return multiplication(b.sprzezenie()).multiplication(1/(b.abs()*b.abs()));
        }

        @Override
        public String toString() {
            return ""+real+" + " +imaginary+"i";
        }
    }

    public MiejscaZerowe(GameStateManager gsm){
        super(gsm);
        bg = new Image(skin.getDrawable("bg"));
        addActor(bg);
        poly = new Complex[MainScreen.getDane().length];
        int t=0;
        for(Double d:MainScreen.getDane()){
            poly[t++] = new Complex(d);
        }

        findRoots();
        Gdx.input.setInputProcessor(this);
        startEnterAnimation();
    }

    private Complex[] countDerive(Complex poly[]){
        Complex derive[] = new Complex[poly.length-1];
        for(int i=0 ; i<derive.length ; i++){
            derive[i]=poly[i].multiplication(derive.length-i);
        }
        return derive;
    }

    private Complex polyValue(Complex value , Complex poly[]){
        Complex odp = new Complex(0);
        for(int i=0 ; i<poly.length ; i++){
            odp = odp.plus(poly[i].multiplication(value.power(poly.length-i-1)));
        }
        return odp;
    }

    private Complex[] findRoots(){
        Complex start = new Complex(-1,0);
        Complex[] z = new Complex[poly.length -1];

        Complex[] polLess;
        z[0] = root(start, poly);

        Complex[] tmp = poly;
        for(int i = 1; i < poly.length -1; i++){
            polLess = newPoly(z[i-1],tmp);
            z[i] = root(start, polLess);
            z[i] = root(z[i], poly);
            tmp = polLess;
        }


        for(int i = 0; i < poly.length -1; i++){
            System.out.println(z[i]);
        }

        return z;
    }

    private Complex root(Complex start , Complex poly[]){
        Complex z = start;
        Complex fun,der,secDer, denominator;
        Complex tmp = new Complex(100);
        while((polyValue(z, poly).minus(polyValue(tmp, poly))).abs() > 1e-15){
            tmp  = z;
            fun = polyValue(z, poly).multiplication(poly.length-1);
            if(fun.getReal() == 0 && fun.getImaginary()==0){
                z = z.minus(new Complex(0));
                continue;
            }
            der = polyValue(z, countDerive(poly));
            secDer = polyValue(z, countDerive(countDerive(poly)));
            denominator = der.multiplication(der).multiplication(poly.length-2).minus(fun.multiplication(secDer).multiplication(poly.length-1)).sqrt();
            denominator = der.minus(denominator).abs() > der.plus(denominator).abs()
                    ? der.minus(denominator) : der.plus(denominator);
            /*if(denominator.real==0){
                z = z.plus(new Complex(1.67));
                continue;
            }*/
            z = z.minus((fun.divide(denominator)));
            int k=90;
        }
        return z;
    }

    public Complex[] newPoly(Complex root, Complex[] oldPoly){
        Complex[] newPoly = new Complex[oldPoly.length-1];
        newPoly[0] = oldPoly[0];
        for(int i = 1; i < newPoly.length; i++){
            newPoly[i] = oldPoly[i].plus(root.multiplication(newPoly[i-1]));
        }
        return newPoly;
    }

    @Override
    public void handleInput(float x, float y) {

    }

    @Override
    public void render(SpriteBatch sb) {
        act();
        draw();
    }
}
