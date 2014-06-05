#include <iostream>
#include <math.h>
#include <fstream>
#include <string>
#include <cstdlib>
#include <vector>
#include <algorithm>
using namespace std;

double distance(vector<int> list, int point1, int point2, int n);
int nearMiss(vector<int> list,vector<string> classes, int point, int n, int m);
int nearHit(vector<int> list,vector<string> classes, int point, int n, int m);

int main(int argc, char* argv[])
{
	bool debug = false;
	if(argc > 1)
	{
		if(strcmp(argv[1], "dbg") == 0)
		{
			debug = true;
			cout << "debugging enabled\n";
		}
	}
	vector<int> list;
	vector<string> classes;
	vector<double> weights;
	string file, data;
	int n, m, i, j, hit, miss, x;
	double weight;
	cout << "Enter your sample size: ";
	cin >> m;
	cout << "Enter your number of features: ";
	cin >> n;
	list.resize(n*m);
	classes.resize(m);
	weights.resize(n);
	cout << "What is the name of your text file? ";
	cin >> file;
	cout << endl << endl;
	ifstream myFile(file.c_str());
	 if (!myFile.fail())
	{
		for(j = 0; j < m; j++)
		{
			for (i = j*n; i < (j*n)+n; i++)
			{
				myFile >> list[i];
				if(debug) {cout << "Value in list[i] " << list[i] << endl;}
				myFile.ignore();
			}
			myFile >> classes[j];
			if(debug) {cout << "Value in classes[j] " << classes[j] << endl << endl;}
			myFile.ignore();
		} // end of file reading loops

		for (i = 0; i < n; i++)
		{
			weight = 0.0;

			for (j = 0; j < m; j++)
			{
				x = (1.0*list[(n*j)+i]);

				if(debug) {cout << "x: " << x << endl;}
				hit = (nearHit(list,classes,j,n,m)*n)+i;
				hit = (1.0*list[hit]);

				if(debug) {cout << "hit: " << hit << endl;}
				miss = (nearMiss(list,classes,j,n,m)*n)+i;

				if(debug) {cout << "nearMiss output: " << nearMiss(list,classes,j,n,m) << endl;}

				if(debug) {cout << "miss coord: " << miss << endl << "i: " << i << " j: " << j << endl;}
				miss = (1.0*list[miss]);

				if(debug) {cout << "miss: " << miss << endl;}
				hit = pow(x - hit,2.0);

				if(debug) {cout << "hit: " << hit << endl;}
				miss = pow(x - miss,2.0);

				if(debug) {cout << "miss: " << miss << endl;}
				weight = weight - hit + miss;

				if(debug) {cout << "weight: " << weight << endl << endl;}
				cin.get();
			}
			weight = weight/(1.0*m);
			weights[i] = weight;
			cout << "W(F" << i+1 << ") = " << weights[i] << endl << endl;

		}

		cout << "\nFeatures Ranking:\n\n";
		sort(weights.begin(), weights.end());
		for(i = n-1, j = 1; i >= 0;i--, j++)
		{ 
			cout << j << ". " << weights[i] << endl;
		}
		cin.get();

	}

	else
	{
		cout << file << "\nThe file could not be opened\n";
		cin.get();
		exit(1);
	}

	return 1;
}

double distance(vector<int> list, int point1, int point2, int n)
{
	double distance = 0;
	for (int i = 0;i < n;i++)
	{
		distance = distance + pow((list[i + ((point1) * n)]) - (list[i + ((point2) * n)]),2.0);
	}
	distance = sqrt(distance);
	return distance;
}

int nearMiss(vector<int> list,vector<string> classes, int point, int n, int m)
{
	int minNum;
	double minDist = 1000000.0, temp;
	for (int i = 0; i < m; i++)
	{
		if (classes[point] != classes[i])
		{
			temp = distance(list, point, (i), n);
			if ( temp < minDist)
			{ minNum = i; minDist = temp; }
		}
	}
	return minNum; // returns int for list vector
}

int nearHit(vector<int> list,vector<string> classes, int point, int n, int m)
{
	int minNum;
	double minDist = 1000000.0, temp;
	for (int i = 0; i < m; i++)
	{
		if (classes[point] == classes[i] && point != i)
		{
			temp = distance(list, point, (i), n);
			if ( temp < minDist)
			{ minNum = i; minDist = temp; }
		}
	}
	return minNum; // returns int for list vector
}