#include <stdbool.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>

void print(const char *s) {printf("%s", s);}

void println(const char *s) {printf("%s\n", s);}

void printInt(int n) {printf("%d", n);}

void printlnInt(int n) {printf("%d\n", n);}

void *_malloc(int n) {return malloc(n);}

const char *getString()
{
	char *stringMid = (char *)(malloc(sizeof(char) * 1024));
	scanf("%s", stringMid);
	return stringMid;
}

int getInt()
{
	int intMid;
	scanf("%d", &intMid);
	return intMid;
}

const char *toString(int i)
{
	char *stringMid = (char*)malloc(sizeof(char) * 15);
	sprintf(stringMid, "%d", i);
	return stringMid;
}

//前面是内建函数

const char *stringAdd(const char *lhs, const char *rhs)
{
	char *stringMid = (char*)malloc(sizeof(char) * 1024);
	strcpy(stringMid, lhs);
	strcat(stringMid, rhs);
	return stringMid;
}

bool stringIsEqual(const char *lhs, const char *rhs) {return strcmp(lhs, rhs) == 0;}

bool stringIsNotEqual(const char *lhs, const char *rhs) {return strcmp(lhs, rhs) != 0;}

bool stringIsLessThan(const char *lhs, const char *rhs) {return strcmp(lhs, rhs) < 0;}

bool stringIsGreaterThan(const char *lhs, const char *rhs) {return strcmp(lhs, rhs) > 0;}

bool stringIsLessThanOrEqual(const char *lhs, const char *rhs) {return strcmp(lhs, rhs) <= 0;}

bool stringIsGreaterThanOrEqual(const char *lhs, const char *rhs) {return strcmp(lhs, rhs) >= 0;}

int stringLength(const char *s) {return strlen(s);}

const char *stringSubstring(const char *s, int l, int r)
{
	char *stringMid = (char*)malloc(sizeof(char) * (r - l + 1));
	memcpy(stringMid, s + l, r - l);
	stringMid[r - l] = '\0';
	return stringMid;
}

int stringParseInt(const char *s)
{
	int intMid;
	sscanf(s, "%d", &intMid);
	return intMid;
}

int stringOrd(const char *s, int pos) {return s[pos];}

