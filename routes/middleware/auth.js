const jwt = require('jsonwebtoken');

module.exports = (req, res, next) => {
    try {
        //console.log(req.headers.authorization);
        const token = req.headers.auth;
        const decoded = jwt.verify(token, process.env.JWT_KEY || 'miClave');
        req.userData = decoded;
        next();
    } catch (error) {
        return res.status(401).json({
            message: 'no esta autorizado para realizar esta operacion'
        });
    }
};
