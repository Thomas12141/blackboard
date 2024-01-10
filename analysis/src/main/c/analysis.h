#ifdef __cplusplus
extern "C" {
#endif
/** declare function pointer f(x). */
typedef double (*function)(double x);
int isLoaded();
/**
* Calculate f'(x) = df/dx at point x within given accuracy.
*/
double differentiate(function f , double x, double accuracy);
/**
*             b
*            /
* Calculate / f(x) dx = F(b)−F(a) within given accuracy.
*          /
*         a
*/
double integrate(function f, double a, double b, double accuracy);
/**
* Access to a zero terminated char array with possible errors
* during the calculations as C has no exceptions.
*/
char* errors();
#ifdef __cplusplus
}
#endif