function [xm,ym,alpha,M,d,res,cond1,cond2, theta]=procrustes(xi,yi,findAlpha)
%PROCRUSTES Solve the Procrustes problem.
%
%From S�derkvist, Wedin, 1993, 'Determining the Movements of the Skeleton
%Using Well-Configured Markers', Journal of Biomechanics 26(12), 1473-1477.
%
%[xm,ym,alpha,M,d,res,cond1,cond2]=procrustes(xi,yi[,findAlpha])
%xi        - Coordinates in the 'from' coordinate system.
%yi        - Coordinates in the 'to' coordinate system.
%findAlpha - if supplied and non-zero, an optimal alpha is determined.
%            Otherwise, alpha is set to 1.
%xm        - mean value of xi.
%ym        - mean value of yi.
%alpha     - resulting scaling factor.
%M         - resulting rotation matrix.
%d         - resulting translation vector.
%res       - transformed residuals.
%cond1     - condition number using approximated singular values.
%cond2     - condition number using true singular values.

% v1.0  19xx-xx-xx. Niclas Borlin, niclas@cs.umu.se.
% v1.1  2000-01-03. Added calculation of condition numbers.
% v1.11 2000-03-26. Corrected bug in approximation of condition number.
% v1.111 2017-05-07. Output of degree of M (SAT)

if (nargin<3)
    findAlpha=0;
end

[m,n]=size(xi);

xm=mean(xi,2);
ym=mean(yi,2);

A=xi-xm(:,ones(1,n));
B=yi-ym(:,ones(1,n));
F=B*A';
[U,S,V]=svd(F);
M=U*V';
if (abs(det(M)-(-1))<10*eps)
    M=eye(m);
    M(m,m)=-1;
    M=U*M*V';
end

if (findAlpha)
    gamma=trace((M*A)'*B);
    alpha=gamma/trace(A'*A);
else
    alpha=1;
end

d=ym-alpha*M*xm;
res=alpha*M*A-B;

s=diag(S);
if (sum(s(2:end))==0)
    cond1=inf;
else
    cond1=1000*1/sqrt(sum(s(2:end)));
end

if (nargout>7)
    s=svd(A);
    cond2=1000*1/sqrt(sum(s(2:end).^2));
end

if (nargout > 8)
    D=eig(M);
    [~, k]=max(abs(D-1));
    theta=angle(D(k));
end

end

