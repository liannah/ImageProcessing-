import ij.ImagePlus;
import ij.process.ImageProcessor;
import ij.plugin.filter.PlugInFilter;
import java.awt.Color;


public class pluginblue implements PlugInFilter {

public int setup(String args, ImagePlus im) {
	return DOES_ALL;
}

public void run(ImageProcessor ip) {

int M = ip.getWidth();
 int N = ip.getHeight();
int l = 256;

int[] H = ip.getHistogram();
double[] H1 = new double[]{3,7,7,53,168,336,539,824,945,1223,
1447,1417,1497,1502,1591,1554,1601,1619,1528,1446,1334,1298,
1275,1177,1151,1034,1096,1032,997,1063,1004,957,1006,925,
896,861,896,902,921,893,889,975,1004,945,940,858,886,863,814,848,
842,792,845,812,839,793,858,846,924,854,830,783,772,845,804,
811,776,733,744,743,766,772,768,742,647,712,652,614,594,
578,547,490,541,541,600,605,632,711,724,782,743,668,694,581,580,
542,490,436,350,313,268,257,172,130,120,94,85,63,48,
49,37,27,29,26,34,26,28,39,62,100,152,162,186,159,181,186,151,169,
123,84,59,55,51,41,42,26,36,29,34,45,28,28,44,29,31,29,49,36,37,
31,28,28,23,29,34,
33,29,32,32,31,26,26,33,18,26,29,27,27,24,14,27,28,23,24,22,31,32,
24,27,26,41,38,64,74,100,110,147,204,241,271,302,264,282,280,
264,210,180,145,113,83,64,72,63,66,70,98,139,150,124,162,132,134,
144,113,76,37,13,1,1,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
};

double[] Blue = new double[H.length];
Blue[0] = H[0];

for (int i = 1; i < H.length; i++){
    H[i] = H[i-1] + H[i];
    Blue[i] = H[i];	
    H1[i] = H1[i -1] + H[i];	
}

for (int i = 1; i < l; i++){
     Blue[i] = Blue[i] / Blue[l-1];
     H1[i] = H1[i] / H1[l-1]; 	
}


int[] result = matchHistograms(Blue, H1);

for (int v = 0; v < N; v++) {
 for (int u = 0; u < M; u++) {
 int a = ip.get(u, v);
 int b = result[a];
 ip.set(u, v, b);
}
}

}

public int[] matchHistograms (double[] hA, double[] hR) {

int K = hA.length;
 double[] PA = Cdf(hA); // get CDF of histogram hA
 double[] PR = Cdf(hR); // get CDF of histogram hR
 int[] fhs = new int[K]; // mapping fhs()

 // compute mapping function fhs():
 for (int a = 0; a < K; a++) {
 int j = K - 1;
 do {
 fhs[a] = j;
 j--;
 } while (j >= 0 && hA[a] <= hR[j]);
 }
 return fhs;
 }



public double[] Cdf (double[] h) {
 // returns the cumul. distribution function for histogram h
 int K = h.length;

 int n = 0; // sum all histogram values
 for (int i = 0; i < K; i++) {
 n += h[i];
 }

 double[] P = new double[K]; // create CDF table P
 double c = h[0]; // cumulate histogram values
 P[0] = (double) c / n;
 for (int i = 1; i < K; i++) {
 c += h[i];
 P[i] = (double) c / n;
 }
 return P;
 }


}