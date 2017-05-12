function [f,J, JJ] = resid(P_,Hom)
m = size(Hom{1},1) / 9;
n = length(P_) / 9;
[row, col] = Hom{2:3};
IJ = [row,col];

P = reshape(P_, 3, 3, []);
H = reshape(Hom{1}, 3, 3, []);

% Pre-allocate residual vector.
f=nan(3,3,m);

for k=1:m
    i=IJ(k,1);
    j=IJ(k,2);
    f(:,:,k)=P(:,:,j)*H(:,:,k)-P(:,:,i);
end

f=reshape(f,9,[]);
f=reshape(f(1:9,:),[],1);

if nargout>2
    JJ=jacapprox(mfilename,P_,1e-6,{Hom});
end

if nargout>1
    % For each 9-block row:
    %  df/dPi=-eye(9)
    %  df/dPj=kron(H(:,:,k)',eye(3))
    J=sparse(length(f),n * 9);
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
% 
% r = zeros(size(row,1)*9,1);
% J = zeros(size(row,1)*9,n);
% for i = 1:size(row,1)
%     i_ = i*9-8:i*9;
%     row_ = row(i)*9-8:row(i)*9;
%     col_=col(i)*9-8:col(i)*9;
%     r(i_,1) = reshape( P{col(i)} * H{row(i),col(i)}  -  P{row(i)} ,9,1);
% %     r(i_,1) = reshape( H{row(i),col(i)} * P{row(i)}  -  P{col(i)} ,9,1);
%     J(i_,row_)=J(i_,row_) - eye(9);
%     J(i_,col_)=J(i_,col_) + diag(reshape(H{row(i),col(i)},9,1));
% end
% 
% if nargout > 2
% end

end