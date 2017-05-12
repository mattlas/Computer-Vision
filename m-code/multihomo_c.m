function [c,A,AA]=multihomo_c(x,H,IJ,ref)
%MULTIHOMO_C Constraint/jacobian function for the homography adjustment problem.
%
%   MULTIHOMO_F(X,H,IJ,REF) returns the constraint function for
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
%   [c,A]=... also returns the analytical Jacobian A of c.
%
%   [c,A,AA]=... also returns a numerical approximation AA of the
%   analytical Jacobian of f. 

m=size(H,3);
n=length(x)/9;

% Reshape P by 9-by-M for simpler computations.
P=reshape(x,9,[]);

% Frobienius constraint.
c1=sum(P.^2,1)'-1;

% Remove Frobenius constraint on reference matrix.
c1(ref)=[];

% Reference constraint.
c2=P(:,ref)-reshape(eye(3),9,[]);

%c=[c1;c2];

c=[c2];

if nargout>2
    AA=jacapprox(mfilename,x,1e-6,{H,IJ,ref});
end

if nargout>1
    % Frobenius part has is block diagonal with blocks 2P(:,i)'.
    A1=sparse(repmat(1:n,9,1),1:n*9,2*P,n+9,n*9);
    % Remove frobenius part for ref.
    A1(ref,:)=[];
    % Reference part has 9-by-9 blocks [0 ... 0 I 0 ... 0].
    A2=sparse(1:9,(ref-1)*9+(1:9),1,9,n*9);
    %A=[A1;A2];
    A=A2;
end
