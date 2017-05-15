function [f,J,JJ]=multihomo_f(x,H,IJ,ref)
%MULTIHOMO_F Residual/jacobian function for the homography adjustment problem.
%
%   MULTIHOMO_F(X,H,IJ,REF) returns the residual function for
%
%       sum      norm(Pj * Hij - Pi,'fro')
%   (i,j) in IJ
%
%          s.t.  norm(Pi,'fro')-1=0
%                Pref = I
%
%   The unknown vector X contains the unrolled 3-by-3 homographies Pi:
%   x=[P1(:);P2(:);...;PN(:)]; Each Pi is the transformation from the
%   image i to the reference image. The 3-by-3-by-M array H contain
%   the pairwise homographies. The homography H(:,:,k) corresponds to
%   the homography between image i and j, where [i,j] is found in row
%   k of the M-by-2 array IJ. REF (unused) is the number of the
%   reference image.
%
%   [f,J]=... also returns the analytical Jacobian J of f.
%
%   [f,J,JJ]=... also returns a numerical approximation JJ of the
%   analytical Jacobian of f. 

m=size(H,3);
n=length(x)/9;

% Reshape P to 3-by-3-by-M.
P=reshape(x,3,3,[]);

% Pre-allocate residual vector.
f=nan(3,3,m);

for k=1:m
    i=IJ(k,1);
    j=IJ(k,2);
    f(:,:,k)=P(:,:,j)*H(:,:,k)-P(:,:,i);
%     f(:,:,k)=H(:,:,k)*P(:,:,j)-P(:,:,i);
end

f=reshape(f,9,[]);
f=reshape(f(1:9,:),[],1);

if nargout>2
    JJ=jacapprox(mfilename,x,1e-6,{H,IJ,ref});
end

if nargout>1
    % For each 9-block row:
    %  df/dPi=-eye(9)
    %  df/dPj=kron(H(:,:,k)',eye(3))
    J=sparse(length(f),length(x));
    for k=1:m
        % Indices for block row.
        i=(k-1)*9+(1:9);
        % Indices for Pi block column.
        ji=(IJ(k,1)-1)*9+(1:9);
        % Indices for Pj block column.
        jj=(IJ(k,2)-1)*9+(1:9);
        J(i,ji)=-speye(9);
        J(i,jj)=kron(H(:,:,k)',speye(3));
    end
end
