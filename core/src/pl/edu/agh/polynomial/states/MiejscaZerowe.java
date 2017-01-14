package pl.edu.agh.polynomial.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;


import pl.edu.agh.polynomial.Polynomial;

import static pl.edu.agh.polynomial.Polynomial.skin;


/**
 * Created by Piotr Muras on 10.12.2016.
 */

public class MiejscaZerowe extends State {
    private Complex poly[];
    private Image bg;
    private static Complex roots[];
    private static Complex droots[];
    private Label showRoots[] , title;
    private BitmapFont sofiaProSoftMedium34px = new BitmapFont(Gdx.files.internal("SofiaProSoftMedium34px.fnt"));
    private BitmapFont sofiaProSoftMedium46px = new BitmapFont(Gdx.files.internal("SofiaProSoftMedium46px.fnt"));
    private Image wstecz;

    public static Complex[] getRoots() {
        return roots;
    }

    public static Complex[] getDroots() { // pierwiastki pochodnej
        return droots;
    }

    public static class Complex {
        private double real;
        private double imaginary;

        public Complex(double real, double imaginary) {
            this.imaginary = imaginary;
            this.real = real;
        }

        public Complex(double real) {
            this.real = real;
            imaginary = 0;
        }

        public Complex(Complex a) {
            real = a.getReal();
            imaginary = a.getImaginary();
        }

        public double getReal() {
            return real;
        }

        public double getImaginary() {
            return imaginary;
        }

        public Complex multiplication(Complex b) {
            Complex a = new Complex(real * b.getReal() - imaginary * b.getImaginary(), real * b.getImaginary() + imaginary * b.getReal());
            return a;
        }

        public Complex multiplication(double b) {
            Complex a = new Complex(real * b, imaginary * b);
            return a;
        }

        public Complex power(int b) {
            if (b == 0) {
                return new Complex(1);
            }
            Complex a = new Complex(this);
            for (int i = 0; i < b - 1; i++) {
                a = a.multiplication(this);
            }
            return a;
        }

        public Complex plus(Complex b) {
            Complex a = new Complex(real + b.getReal(), imaginary + b.getImaginary());
            return a;
        }

        public Complex minus(Complex b) {
            Complex a = new Complex(real - b.getReal(), imaginary - b.getImaginary());
            return a;
        }

        public double abs() {
            return Math.sqrt(real * real + imaginary * imaginary);
        }

        public double arg() {
            double argument;
            if (real > 0) {
                argument = Math.atan(imaginary / real);
            } else {
                if (real < 0) {
                    argument = Math.atan(imaginary / real) + Math.PI;
                } else {
                    if (imaginary > 0) {
                        argument = Math.PI / 2;
                    } else {
                        argument = -Math.PI / 2;
                    }
                }
            }
            if (argument < 0) {
                argument += 2 * Math.PI;
            }
            return argument;
        }

        public Complex sqrt() {
            if (real == 0 && imaginary == 0) {
                return new Complex(0);
            }
            return new Complex(abs() / 2 * Math.cos(arg() / 2), abs() / 2 * Math.sin(arg() / 2));
        }

        public Complex sprzezenie() {
            return new Complex(real, -imaginary);
        }

        public Complex divide(Complex b) {
            return multiplication(b.sprzezenie()).multiplication(1 / (b.abs() * b.abs()));
        }

        @Override
        public String toString() {
            return "" + real + " + " + imaginary + "i";
        }
    }

    public static void licz(){
        double poly[] = new double[MainScreen.getDane().length];
        int t = 0;
        for (Double d : MainScreen.getDane()) {
            poly[t++] = d;
        }

        roots = new Complex[poly.length-1];
        roots = findRoots(poly);


        droots = new Complex[poly.length-2];
        droots = findRoots(countDerive(poly));
    }

    public MiejscaZerowe(GameStateManager gsm) {
        super(gsm);
        bg = new Image(skin.getDrawable("bg"));
        addActor(bg);
        licz();
        Gdx.input.setInputProcessor(this);
        startEnterAnimation();
        title = new Label("Miejsca zerowe" , new Label.LabelStyle(sofiaProSoftMedium46px , Color.BLACK));
        title.setPosition(Polynomial.WIDTH/2 -title.getWidth()/2, upY(60));
        addActor(title);
        showRoots = new Label[roots.length];
        for(int i=0 ; i<showRoots.length ; i++){
            if(Math.round(MiejscaZerowe.getRoots()[i].getImaginary()*100.0)/100.0!=0) {
                showRoots[i] = new Label("" + Math.round(MiejscaZerowe.getRoots()[i].getReal() * 100.0) / 100.0 + " +  " + Math.round(MiejscaZerowe.getRoots()[i].getImaginary() * 100.0) / 100.0 + "i", new Label.LabelStyle(sofiaProSoftMedium34px, Color.BLACK));
            }
            else showRoots[i] = new Label("" + Math.round(MiejscaZerowe.getRoots()[i].getReal() * 100.0) / 100.0, new Label.LabelStyle(sofiaProSoftMedium34px, Color.BLACK));

            showRoots[i].setPosition(30+350*(i/8) , upY(120+(i%8)*39));
            addActor(showRoots[i]);
        }
        wstecz=new Image(Polynomial.skin.getDrawable("wstecz"));
        wstecz.setPosition(0,0);
        addActor(wstecz);
    }

    private static double[] countDerive(double poly[]) {
        double derive[] = new double[poly.length - 1];
        for (int i = 0; i < derive.length; i++) {
            derive[i] = poly[i]*(derive.length - i);
        }
        return derive;
    }

    @Override
    public void handleInput(float x, float y) {
        if((x-wstecz.getX()-wstecz.getWidth()/2)*(x-wstecz.getX()-wstecz.getWidth()/2) + (y-upY((int)wstecz.getY())+wstecz.getHeight()/2)*(y-upY((int)wstecz.getY())+wstecz.getHeight()/2) < wstecz.getWidth()/2*wstecz.getWidth()/2) {

            startEndAnimationAndPopState();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        act();
        draw();
    }

    public static Complex[] findRoots(double[] coeffs) {
        PolynomialSolver polynomialSolver = new PolynomialSolver();
        return polynomialSolver.rpoly(coeffs);
    }

    private static class PolynomialSolver {
        //algorytm Jenkinsa-Trauba

        private static final double eta = 2.22E-16;
        private static final double base = 2;
        private static final double infin = Float.MAX_VALUE;
        private static final double smalno = Float.MIN_NORMAL;
        private static final double are = eta;
        private static final double mre = eta;
        private static final double rotationAngleDeg = 94;
        private static final double rotationAngle = rotationAngleDeg / 360 * 2 * Math.PI;
        private final double cosr = Math.cos(rotationAngle);
        private final double sinr = Math.sin(rotationAngle);
        private int n;
        private double[] p, qp, k, qk;
        private double u, v, a, b, c, d, a1, a3, a7, e, f, g, h;
        private Complex sz;
        private Complex lz;

        private Complex[] rpoly(double[] wspolczynniki) {
            final int degree = wspolczynniki.length - 1;
            Complex[] zeros = new Complex[degree];
            final double lo = smalno / eta;
            double xx = Math.sqrt(0.5);
            double yy = -xx;
            n = degree;
            while (n > 0 && wspolczynniki[n] == 0) {
                zeros[degree - n] = new Complex(0, 0);
                n--;
            }
            p = new double[n + 1 + 1];
            for (int i = 1; i <= n + 1; i++) {
                p[i] = wspolczynniki[i - 1];
            }
            while (true) {
                if (n < 1) {
                    return zeros;
                }
                if (n == 1) {
                    zeros[degree - 1] = new Complex(-p[2] / p[1]);
                    return zeros;
                }
                if (n == 2) {
                    Complex[] temp1 = quad(p[1], p[2], p[3]);
                    zeros[degree - 2] = temp1[0];
                    zeros[degree - 1] = temp1[1];
                    return zeros;
                }
                double max = 0;
                double min = infin;
                for (int i = 1; i <= n + 1; i++) {
                    double x = Math.abs(p[i]);
                    if (x > max) {
                        max = x;
                    }
                    if (x != 0 && x < min) {
                        min = x;
                    }
                }
                double sc = lo / min;
                if (sc == 0) {
                    sc = smalno;
                }
                if ((sc > 1 && infin / sc >= max) || (sc <= 1 && max > 10)) {
                    double l = Math.log(sc) / Math.log(base) + 0.5;
                    double factor = Math.pow(base, l);
                    if (factor != 1) {
                        for (int i = 1; i <= n + 1; i++) {
                            p[i] = factor * p[i];
                        }
                    }
                }
                double[] pt = new double[n + 1 + 1];
                for (int i = 1; i <= n + 1; i++) {
                    pt[i] = Math.abs(p[i]);
                }
                pt[n + 1] = -pt[n + 1];
                double x = Math.exp((Math.log(-pt[n + 1]) - Math.log(pt[1])) / n);
                if (pt[n] != 0) {
                    double xm = -pt[n + 1] / pt[n];
                    if (xm < x) {
                        x = xm;
                    }
                }
                while (true) {
                    double xm = x * 0.1;
                    double ff = pt[1];
                    for (int i = 2; i <= n + 1; i++) {
                        ff = ff * xm + pt[i];
                    }
                    if (ff <= 0) {
                        break;
                    }
                    x = xm;
                }
                double dx = x;
                while (Math.abs(dx / x) > 0.005) {
                    double ff = pt[1];
                    double df = ff;
                    for (int i = 2; i <= n; i++) {
                        ff = ff * x + pt[i];
                        df = df * x + ff;
                    }
                    ff = ff * x + pt[n + 1];
                    dx = ff / df;
                    x = x - dx;
                }
                double bnd = x;
                int nm1 = n - 1;
                k = new double[n + 1];
                for (int i = 2; i <= n; i++) {
                    k[i] = (n + 1 - i) * p[i] / n;
                }
                k[1] = p[1];
                double aa = p[n + 1];
                double bb = p[n];
                boolean zerok = k[n] == 0;
                for (int jj = 1; jj <= 5; jj++) {
                    double cc = k[n];
                    if (zerok) {
                        for (int i = 1; i <= nm1; i++) {
                            int j = n + 1 - i;
                            k[j] = k[j - 1];
                        }
                        k[1] = 0;
                        zerok = k[n] == 0;
                    } else {
                        double t = -aa / cc;
                        for (int i = 1; i <= nm1; i++) {
                            int j = n + 1 - i;
                            k[j] = t * k[j - 1] + p[j];
                        }
                        k[1] = p[1];
                        zerok = Math.abs(k[n]) <= Math.abs(bb) * eta * 10;
                    }
                }
                double[] temp = new double[n + 1];
                for (int i = 1; i <= n; i++) {
                    temp[i] = k[i];
                }
                int cnt = 1;
                while (true) {
                    double xxx = cosr * xx - sinr * yy;
                    yy = sinr * xx + cosr * yy;
                    xx = xxx;
                    double sr = bnd * xx;
                    u = -2 * sr;
                    v = bnd;
                    qp = new double[n + 1 + 1];
                    qk = new double[n + 1];
                    int nz = fxshfr(20 * cnt, sr);
                    if (nz > 0) {
                        zeros[degree - n] = sz;
                        if (nz > 1) {
                            zeros[degree - n + 1] = lz;
                        }
                        n = n - nz;
                        for (int i = 1; i <= n + 1; i++) {
                            p[i] = qp[i];
                        }
                        qp = null;
                        qk = null;
                        break;
                    }
                    for (int i = 1; i <= n; i++) {
                        k[i] = temp[i];
                    }
                    if (cnt++ > 20) {
                        throw new RuntimeException("Nie ma zbieżności kurcze");
                    }
                }
            }
        }

        private int fxshfr(int l2, double sr) {
            double ots = 0;
            double otv = 0;
            double betav = 0.25;
            double betas = 0.25;
            double oss = sr;
            double ovv = v;
            double[] temp1 = new double[2];
            quadsd(n + 1, u, v, p, qp, temp1);
            a = temp1[0];
            b = temp1[1];
            int type = calcsc();
            for (int j = 1; j <= l2; j++) {
                nextk(type);
                type = calcsc();
                double[] temp2 = newest(type);
                double ui = temp2[0];
                double vi = temp2[1];
                double vv = vi;
                double ss = 0;
                if (k[n] != 0) {
                    ss = -p[n + 1] / k[n];
                }
                double tv = 1;
                double ts = 1;
                if (j != 1 && type != 3) {
                    if (vv != 0) {
                        tv = Math.abs((vv - ovv) / vv);
                    }
                    if (ss != 0) {
                        ts = Math.abs((ss - oss) / ss);
                    }
                    double tvv = (tv < otv) ? tv * otv : 1;
                    double tss = (ts < ots) ? ts * ots : 1;
                    boolean vpass = tvv < betav;
                    boolean spass = tss < betas;
                    if (spass || vpass) {
                        double svu = u;
                        double svv = v;
                        double[] svk = new double[n + 1];
                        for (int i = 1; i <= n; i++) {
                            svk[i] = k[i];
                        }
                        double s = ss;
                        boolean vtry = false;
                        boolean stry = false;
                        int state = (spass && (!vpass || tss < tvv)) ? 40 : 20;
                        while (state != 70) {
                            switch (state) {
                                case 20: {
                                    int nz = quadit(ui, vi);
                                    if (nz > 0) {
                                        return nz;
                                    }
                                    vtry = true;
                                    betav = betav * 0.25;
                                    if (stry || !spass) {
                                        state = 50;
                                        break;
                                    }
                                    for (int i = 1; i <= n; i++) {
                                        k[i] = svk[i];
                                    }
                                    state = 40;
                                    break;
                                }
                                case 40: {
                                    RealitOut realitOut = realit(s);
                                    if (realitOut.nz > 0) {
                                        return realitOut.nz;
                                    }
                                    s = realitOut.sss;
                                    stry = true;
                                    betas *= 0.25;
                                    if (realitOut.iflag) {
                                        state = 50;
                                        break;
                                    }
                                    ui = -(s + s);
                                    vi = s * s;
                                    state = 20;
                                    break;
                                }
                                case 50: {
                                    u = svu;
                                    v = svv;
                                    for (int i = 1; i <= n; i++) {
                                        k[i] = svk[i];
                                    }
                                    if (vpass && !vtry) {
                                        state = 20;
                                        break;
                                    }
                                    double[] temp3 = new double[2];
                                    quadsd(n + 1, u, v, p, qp, temp3);
                                    a = temp3[0];
                                    b = temp3[1];
                                    type = calcsc();
                                    state = 70;
                                    break;
                                }
                                default:
                                    throw new AssertionError();
                            }
                        }
                    }
                }
                ovv = vv;
                oss = ss;
                otv = tv;
                ots = ts;
            }
            return 0;
        }
        private int quadit(double uu, double vv) {
            boolean tried = false;
            double omp = 0;
            double relstp = 0;
            u = uu;
            v = vv;
            int j = 0;
            while (true) {
                Complex[] zeros = quad(1, u, v);
                sz = zeros[0];
                lz = zeros[1];
                if (Math.abs(Math.abs(sz.getReal()) - Math.abs(lz.getReal())) > 0.01 * Math.abs(lz.getReal())) {
                    return 0;
                }
                double[] temp1 = new double[2];
                quadsd(n + 1, u, v, p, qp, temp1);
                a = temp1[0];
                b = temp1[1];
                double mp = Math.abs(a - sz.getReal() * b) + Math.abs(sz.getImaginary() * b);
                double zm = Math.sqrt(Math.abs(v));
                double ee = 2 * Math.abs(qp[1]);
                double t = -sz.getReal() * b;
                for (int i = 2; i <= n; i++) {
                    ee = ee * zm + Math.abs(qp[i]);
                }
                ee = ee * zm + Math.abs(a + t);
                ee = (5 * mre + 4 * are) * ee -
                        (5 * mre + 2 * are) * (Math.abs(a + t) + Math.abs(b) * zm) +
                        2 * are * Math.abs(t);
                if (mp <= 20 * ee) {
                    return 2;
                }
                j++;
                if (j > 20) {
                    return 0;
                }
                if (j >= 2 && relstp <= 0.01 && mp >= omp && !tried) {
                    if (relstp < eta) {
                        relstp = eta;
                    }
                    relstp = Math.sqrt(relstp);
                    u = u - u * relstp;
                    v = v + v * relstp;
                    double[] temp2 = new double[2];
                    quadsd(n + 1, u, v, p, qp, temp2);
                    a = temp2[0];
                    b = temp2[1];
                    for (int i = 1; i <= 5; i++) {
                        int type = calcsc();
                        nextk(type);
                    }
                    tried = true;
                    j = 0;
                }
                omp = mp;
                int type1 = calcsc();
                nextk(type1);
                int type2 = calcsc();
                double[] temp2 = newest(type2);
                double ui = temp2[0];
                double vi = temp2[1];
                if (vi == 0) {
                    return 0;
                }
                relstp = Math.abs((vi - v) / vi);
                u = ui;
                v = vi;
            }
        }

        private class RealitOut {
            double sss;
            int nz;
            boolean iflag;

            RealitOut(double sss, int nz, boolean iflag) {
                this.sss = sss;
                this.nz = nz;
                this.iflag = iflag;
            }
        }

        private RealitOut realit(double sss) {
            double omp = 0;
            double t = 0;
            double s = sss;
            int j = 0;
            while (true) {
                double pv = p[1];
                qp[1] = pv;
                for (int i = 2; i <= n + 1; i++) {
                    pv = pv * s + p[i];
                    qp[i] = pv;
                }
                double mp = Math.abs(pv);
                double ms = Math.abs(s);
                double ee = (mre / (are + mre)) * Math.abs(qp[1]);
                for (int i = 2; i <= n + 1; i++) {
                    ee = ee * ms + Math.abs(qp[i]);
                }
                if (mp <= 20 * ((are + mre) * ee - mre * mp)) {
                    sz = new Complex(s);
                    return new RealitOut(sss, 1, false);
                }
                j++;
                if (j > 10) {
                    return new RealitOut(sss, 0, false);
                }
                if (j >= 2 && Math.abs(t) <= 0.001 * Math.abs(s - t) && mp > omp) {
                    return new RealitOut(s, 0, true);
                }
                omp = mp;
                double kv = k[1];
                qk[1] = kv;
                for (int i = 2; i <= n; i++) {
                    kv = kv * s + k[i];
                    qk[i] = kv;
                }
                if (Math.abs(kv) <= Math.abs(k[n]) * 10 * eta) {
                    k[1] = 0;
                    for (int i = 2; i <= n; i++) {
                        k[i] = qk[i - 1];
                    }
                } else {
                    t = -pv / kv;
                    k[1] = qp[1];
                    for (int i = 2; i <= n; i++) {
                        k[i] = t * qk[i - 1] + qp[i];
                    }
                }
                kv = k[1];
                for (int i = 2; i <= n; i++) {
                    kv = kv * s + k[i];
                }
                t = 0;
                if (Math.abs(kv) > Math.abs(k[n]) * 10 * eta) {
                    t = -pv / kv;
                }
                s = s + t;
            }
        }

        private int calcsc() {
            double[] temp = new double[2];
            quadsd(n, u, v, k, qk, temp);
            c = temp[0];
            d = temp[1];
            if (Math.abs(c) <= Math.abs(k[n]) * 100 * eta || Math.abs(d) <= Math.abs(k[n - 1]) * 100 * eta) {
                return 3;
            }
            if (Math.abs(d) < Math.abs(c)) {
                e = a / c;
                f = d / c;
                g = u * e;
                h = v * b;
                a3 = a * e + (h / c + g) * b;
                a1 = b - a * (d / c);
                a7 = a + g * d + h * f;
                return 1;
            }
            else {
                e = a / d;
                f = c / d;
                g = u * b;
                h = v * b;
                a3 = (a + g) * e + h * (b / d);
                a1 = b * f - a;
                a7 = (f + u) * a + h;
                return 2;
            }
        }

        private void nextk(int type) {
            if (type == 3) {
                k[1] = 0;
                k[2] = 0;
                for (int i = 3; i <= n; i++) {
                    k[i] = qk[i - 2];
                }
                return;
            }
            double temp = (type == 1) ? b : a;
            if (Math.abs(a1) > Math.abs(temp) * eta * 10) {
                a7 = a7 / a1;
                a3 = a3 / a1;
                k[1] = qp[1];
                k[2] = qp[2] - a7 * qp[1];
                for (int i = 3; i <= n; i++) {
                    k[i] = a3 * qk[i - 2] - a7 * qp[i - 1] + qp[i];
                }
            } else {
                k[1] = 0;
                k[2] = -a7 * qp[1];
                for (int i = 3; i <= n; i++) {
                    k[i] = a3 * qk[i - 2] - a7 * qp[i - 1];
                }
            }
        }

        private double[] newest(int type) {
            if (type == 3) {
                return new double[]{0, 0};
            }
            double a4, a5;
            if (type == 2) {
                a4 = (a + g) * f + h;
                a5 = (f + u) * c + v * d;
            } else {
                a4 = a + u * b + h * f;
                a5 = c + (u + v * f) * d;
            }
            double b1 = -k[n] / p[n + 1];
            double b2 = -(k[n - 1] + b1 * p[n]) / p[n + 1];
            double c1 = v * b2 * a1;
            double c2 = b1 * a7;
            double c3 = b1 * b1 * a3;
            double c4 = c1 - c2 - c3;
            double temp = a5 + b1 * a4 - c4;
            if (temp == 0) {
                return new double[]{0, 0};
            }
            double uu = u - (u * (c3 + c2) + v * (b1 * a1 + b2 * a7)) / temp;
            double vv = v * (1 + c4 / temp);
            return new double[]{uu, vv};
        }

    }

    private static void quadsd(int n, double u, double v, double p[], double[] q, double[] rem) {
        double b = p[1];
        q[1] = b;
        double a = p[2] - u * b;
        q[2] = a;
        for (int i = 3; i <= n; i++) {
            double c = p[i] - u * a - v * b;
            q[i] = c;
            b = a;
            a = c;
        }
        rem[0] = a;
        rem[1] = b;
    }

    private static Complex[] quad(double a, double b, double c) {
        if (a == 0 && b == 0) {
            return new Complex[]{new Complex(0), new Complex(0)};
        }
        if (a == 0) {
            return new Complex[]{new Complex(-c / b), new Complex(0)};
        }
        if (c == 0) {
            return new Complex[]{new Complex(0), new Complex(-b / a)};
        }
        double b2 = b / 2;
        double e, d;
        if (Math.abs(b2) < Math.abs(c)) {
            double e1 = (c >= 0) ? a : -a;
            e = b2 * (b2 / Math.abs(c)) - e1;
            d = Math.sqrt(Math.abs(e)) * Math.sqrt(Math.abs(c));
        } else {
            e = 1 - (a / b2) * (c / b2);
            d = Math.sqrt(Math.abs(e)) * Math.abs(b2);
        }
        if (e >= 0) {
            double d2 = (b2 >= 0) ? -d : d;
            double lr = (-b2 + d2) / a;
            double sr = (lr != 0) ? c / lr / a : 0;
            return new Complex[]{new Complex(sr), new Complex(lr)};
        } else {
            Complex z1 = new Complex(-b2 / a, Math.abs(d / a));
            return new Complex[]{z1, new Complex(z1.getReal(), -z1.getImaginary())};
        }
    }

}

