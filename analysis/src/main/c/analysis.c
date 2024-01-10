#ifdef __cplusplus
extern "C" {
#endif
#include <stdio.h>

typedef double (*function)(double x);

double integralResults[10000] = {0.0};

char errorsArr[200];

int isLoaded() {
    return 1;
}

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
        if(n<0||n>65536){
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

/*int main() {
    double derivative = differentiate(xHoch2, 0.0, 0.000000001);
    printf("%lf\n", derivative); //Muss 0 sein

    derivative = differentiate(xHoch2, 3.0, 0.000000001);
    printf("%lf\n", derivative); //Muss 6 sein

    double integral = integrate(xHoch2, 0, 3.0, 0.000000001);
    printf("%lf\n", integral); //Muss 9 sein

    double sinIntegral = integrate(sin, 0, M_PI, 0.0000001);
    printf("Numerisches Integral von sin(x) von 0 bis PI: %lf\n", sinIntegral); // Muss 2 sein

    double expDerivative = differentiate(exp, 1.0, 0.000000001);
    printf("Numerische Ableitung an x=1.0 von exp(x): %lf\n", expDerivative); // Muss exp(1.0) sein

    double logDerivative = differentiate(log, 2.0, 0.000000001);
    printf("Numerische Ableitung an x=2.0 von log(x): %lf\n", logDerivative); // Muss 0.5 sein

    double sqrtDerivative = differentiate(sqrt, 4.0, 0.000000001);
    printf("Numerische Ableitung an x=4.0 von sqrt(x): %lf\n", sqrtDerivative); // Muss 0.25 sein

    double cosIntegral = integrate(cos, 0, M_PI, 0.0000001);
    printf("Numerisches Integral von cos(x) von 0 bis PI: %lf\n", cosIntegral); // Muss 0 sein

    double sinhDerivative = differentiate(sinh, 0.5, 0.000000001);
    printf("Numerische Ableitung an x=0.5 von sinh(x): %lf\n", sinhDerivative); // Muss cosh(0.5) sein

    double coshIntegral = integrate(cosh, 0, 1.0, 0.0000001);
    printf("Numerisches Integral von cosh(x) von 0 bis 1.0: %lf\n", coshIntegral); // Muss sinh(1.0) sein

    double powDerivative = differentiate(powerFunction, 2.0, 0.000000001);
    printf("Numerische Ableitung an x=2.0 von pow(x,2): %lf\n", powDerivative); // Muss 4.0 sein

    double powIntegral = integrate(powerFunction, 0, 2.0, 0.0000001);
    printf("Numerisches Integral von pow(x,2) von 0 bis 2.0: %lf\n", powIntegral); // Muss 8/3 sein

    double sinExpIntegral = integrate(sinPlusExp, 0, M_PI, 0.0000001);
    printf("Numerisches Integral von sin(x) + exp(x) von 0 bis PI: %lf\n", sinExpIntegral);

    double logSqrtIntegral = integrate(logPlusSqrt, 1.0, 4.0, 0.0000001);
    printf("Numerisches Integral von log(x) + sqrt(x) von 1.0 bis 4.0: %lf\n", logSqrtIntegral);

    // Test mit der Funktion x^3 von 0 bis 2
    double integralXCubed = integrate(xCubed, 0, 2, 0.0000001);
    printf("Numerisches Integral von x^3 von 0 bis 2: %lf\n", integralXCubed);

    // Test mit der Funktion e^{-x} von 0 bis 1
    double integralExpNegX = integrate(expNegX, 0, 1, 0.0000001);
    printf("Numerisches Integral von e^{-x} von 0 bis 1: %lf\n", integralExpNegX);

    // Test mit der Funktion cos(x) von 0 bis PI
    double integralCosX = integrate(cosX, 0, M_PI, 0.0000001);
    printf("Numerisches Integral von cos(x) von 0 bis PI: %lf\n", integralCosX);

    // Test mit der Funktion sin(x) von 0 bis PI
    double integralSinX = integrate(sinX, 0, M_PI, 0.0000001);
    printf("Numerisches Integral von sin(x) von 0 bis PI: %lf\n", integralSinX);

    // Test mit der Funktion sqrt(x) von 1 bis 4
    double integralSqrtX = integrate(sqrtX, 1, 4, 0.0000001);
    printf("Numerisches Integral von sqrt(x) von 1 bis 4: %lf\n", integralSqrtX);


    if (strlen(errorsArr) > 0) {
        printf("\nFehler: %s\n", errorsArr);
    } else {
        printf("\nKeine Fehler!");
    }


    return 0;
}*/
#ifdef __cplusplus
}
#endif