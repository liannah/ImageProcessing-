import ij.ImagePlus;
import ij.process.ImageProcessor;
import ij.plugin.filter.PlugInFilter;
import java.awt.Color;


public class plugingreen implements PlugInFilter {

public int setup(String args, ImagePlus im) {
	return DOES_ALL;
}

public void run(ImageProcessor ip) {

int M = ip.getWidth();
 int N = ip.getHeight();
int l = 256;

int[] H = ip.getHistogram();
double[] H1 = new double[]{0,1,1,42,108,220,294,358,364,499,
555,653,887,1028,1218,1303,1490,1442,1414,1476,1520,1556,
1464,1405,1400,1272,1204,1086,1000,1020,940,874,846,
813,810,760,698,694,677,654,649,616,610,567,568,519,547,520,
510,486,485,510,493,519,558,563,561,557,570,603,615,634,594,626,
599,600,611,629,599,573,581,550,556,605,548,549,575,587,668,
668,694,634,715,699,698,715,726,764,736,717,737,740,687,690,694,
684,666,709,731,722,637,669,653,600,651,620,649,572,655,767,826,
854,806,746,695,621,573,550,485,421,357,327,273,298,248,244,
211,180,156,167,119,120,100,73,52,51,35,40,48,34,42,40,30,47,40,
37,36,41,45,37,38,36,35,35,27,29,27,34,30,38,24,29,27,25,22,22,
32,32,24,20,27,34,34,25,39,24,35,33,47,49,73,80,102,143,
160,219,277,315,307,291,276,268,245,193,157,154,92,89,98,85,144,
179,127,100,110,144,189,140,95,42,11,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};


double[] Green = new double[H.length];
Green[0] = H[0];

for (int i = 1; i < H.length; i++){
    H[i] = H[i-1] + H[i];
    Green[i] = H[i];	
    H1[i] = H1[i -1] + H1[i];	
}

for (int i = 0; i < l; i++){
     Green[i] = Green[i] / Green[l-1];
     H1[i] = H1[i] / H1[l-1]; 	
}

for(int i =0; i < l; i++){
ij.IJ.log("Hist" + " " + H1[i]);

}

int[] result = matchHistograms(Green, H1);

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