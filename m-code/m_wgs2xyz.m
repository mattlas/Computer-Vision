function [XYZ] = m_wgs2xyz( LLA )
% WGS84 coordinates to XYZ converter to help with DBAT.
% 	INPUT: Column vectors with columns: Latitude, Longitude and Altitude data
% 	OUTPUT: Column vectors with columns containing X, Y, Z data
    
    La = LLA( :, 1);
    Lo = LLA( :, 2);
    Al = LLA( :, 3);
    cLa = cos(La * pi / 180);
    sLa = sin(La * pi / 180);
    cLo = cos(Lo * pi / 180);
    sLo = sin(Lo * pi / 180);
    r = 6378137; % Semi-major axis of Earth
    f = 1 / 298.257224; % Flattening parameter
    C = 1 ./ sqrt(cLa .* cLa + (1 - f) .* (1 - f) .* sLa .* sLa);
    S = (1 - f) * (1 - f) * C;
    X = ( r .* C + Al) .* cLa .* cLo;
    Y = ( r .* C + Al) .* cLa .* sLo;
    Z = ( r .* S + Al) .* sLa;
    XYZ = [X, Y, Z];
    
    % Plotting
%     figure(2)
%     scatter3(pos(:,1)', pos(:,2)',  pos(:, 3))
%     axis equal
end
