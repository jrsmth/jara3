const { Router } = require('express');
const router = Router();

const { check } = require('express-validator');
const { authenticate, register } = require('../controllers/auth');

router.post('/authenticate',[
    check('username', 'Username is required').not().isEmpty(),
    check('password', 'Password is required').not().isEmpty(),
], authenticate );

router.post('/register',[
    check('username', 'Username is required').not().isEmpty(),
    check('password', 'Password is required').not().isEmpty(),
], register );

module.exports = router;