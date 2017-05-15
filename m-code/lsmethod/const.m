function [c,A, AA] = const(P_,Hom)
% Constraint function for mosaic
m = size(Hom{1},1) / 9;
n = length(P_) / 9;


% Reshape P by 9-by-M for simpler computations.
P=reshape(P_,9,[]);

% Frobienius constraint.
c1=sum(P.^2,1)'-1;

% Remove Frobenius constraint on reference matrix.
c1(1)=[];

% Reference constraint.
c2=P(:,1)-reshape(eye(3),9,[]);

%c=[c1;c2];

c=[c2];

if nargout>2
    AA=jacapprox(mfilename,P_,1e-6,{Hom});
end

if nargout>1
    % Frobenius part has is block diagonal with blocks 2P(:,i)'.
    A1=sparse(repmat(1:n,9,1),1:n*9,2*P,n+9,n*9);
    % Remove frobenius part for ref.
    A1(1,:)=[];
    % Reference part has 9-by-9 blocks [0 ... 0 I 0 ... 0].
    A2=sparse(1:9,(1-1)*9+(1:9),1,9,n*9);
    %A=[A1;A2];
    A=A2;
end
% c = zeros( n/9 + 9 , 1 );
% for ids = 1:n/9
%     c(ids) = P(ids*9) - 1;
% end
% c(end-8:end) = P(1:9) - reshape(eye(3),[],1);
% %c(end) = norm(P{1} - eye(3),2);
% 
% J=sparse(n/9+9,n);
% for i = 1:n/9
%     J(i,i*9) = 1;
% end
% J(end-8:end,1:9) = eye(9);
% 
% if nargout > 2
%     JJ=jacapprox(mfilename,P,1e-6,{n});
% end
% end