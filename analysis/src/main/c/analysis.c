#ifdef __cplusplus
extern "C" {
#endif
#include <stdio.h>

typedef double (*function)(double x);

double integralResults[10000] = {0.0};

char errorsArr[200];

double betrag(double d) {
    if(d>0){
        return d;
    }
    return -d;
}

double differentiateHelper(function f, double x, double x0, double h){
    return (f(x)-f(x0))/(2*h);
}

double differentiate(function f , double x, double accuracy){
    double h = 0.1;
    double fh = differentiateHelper(f, x+h, x-h, h);
    double f2h = differentiateHelper(f, x+2*h, x-2*h,2*h);
    double diff = betrag(fh-f2h);
    while (diff >= accuracy){
        if(h==0.0){
            sprintf(errorsArr, "Underflow in the while loop of differentiate\n");
            break;
        }
        h/=10;
        fh = differentiateHelper(f, x+h, x-h, h);
        f2h = differentiateHelper(f, x+2*h, x-2*h,2*h);
        if(diff <= betrag(fh-f2h)){
            sprintf(errorsArr, "No convergence in differentiate\n");
            break;
        }
        diff = betrag(fh-f2h);
    }
    return f2h;
}



double integrate(function f, double a, double b, double accuracy){
    double tn = (f(a)+f(b))/2.0;// n = 0
    double t2n = tn*(b-a); // n = 1
    double diff;
    int n = 1;
    do{
        diff = betrag(t2n-tn);
        n*= 2;
        if(n<0){
            sprintf(errorsArr, "Overflow in the while loop of integrate\n");
            break;
        }
        tn = t2n;
        double temp = 0;
        double h = (b-a)/n;
        for (int i = 0; i < n; ++i) {
            temp+= f(a + h/2.0 + i*h);
        }
        temp*=h;
        t2n = (tn +temp)/2.0;
        if(diff <= betrag(t2n - tn)){
            sprintf(errorsArr, "No convergence in the while loop of integrate\n");
            break;
        }
    } while (diff >= accuracy);
    return (4*t2n-tn)/3.0;
}

double xHoch2(double x){
    return x*x;
}

char* errors(){
    return errorsArr;
}
#ifdef __cplusplus
}
#endif